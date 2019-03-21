package net.chat.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatServerChannelHandler extends SimpleChannelInboundHandler<String> {
    private Logger logger= LoggerFactory.getLogger(ChatServerChannelHandler.class);
    private static ChannelGroup channelGroup=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        logger.info("handlerAdded: "+ctx.channel().remoteAddress());
        channelGroup.writeAndFlush("【"+ctx.channel().remoteAddress()+"】： 欢迎加入！\n");
        channelGroup.add(ctx.channel());
        super.handlerAdded(ctx);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        logger.info("channelRegistered: "+ctx.channel().remoteAddress());
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        logger.info("channelUnregistered: "+ctx.channel().remoteAddress());
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel1 = ctx.channel();
        logger.info("【"+ channel1.remoteAddress()+"】: channelActive");
        channelGroup.forEach(channel -> {
            if(channel!= channel1){
                channel.writeAndFlush("【"+ channel1.remoteAddress()+"】 上线了！\n");
            }
        });
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel ch = ctx.channel();
        logger.info("channelInactive: "+ ch.remoteAddress());
//        channelGroup.writeAndFlush("【+"+ctx.channel().remoteAddress()+"+】 离线了！");
        channelGroup.forEach(channel -> {
            if(channel!= ch){
                channel.writeAndFlush("【"+ ch.remoteAddress()+"】： 离线了！\n");
            }
        });
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel ch = ctx.channel();
        logger.info("【"+ ch.remoteAddress()+"】: "+msg);
        channelGroup.forEach(channel -> {
            if(channel!= ch){
                channel.writeAndFlush("【"+ ch.remoteAddress()+"】："+msg+"\n");
            }else{
                channel.writeAndFlush("【自己】： "+ msg+"\n");
            }
        });
//        super.channelRead(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.info("exceptionCaught: "+ctx.channel().remoteAddress());
//        cause.printStackTrace();
        ctx.close();
//        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        logger.info("handlerRemoved: "+ctx.channel().remoteAddress());
        channelGroup.writeAndFlush("【服务器】-"+ctx.channel().remoteAddress()+" 下线了！\n");
        super.handlerRemoved(ctx);
    }
}
