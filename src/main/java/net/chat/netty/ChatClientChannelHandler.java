package net.chat.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatClientChannelHandler extends SimpleChannelInboundHandler<String> {
    Logger logger= LoggerFactory.getLogger(ChatClientChannelHandler.class);
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        logger.info(msg);
    }

}
