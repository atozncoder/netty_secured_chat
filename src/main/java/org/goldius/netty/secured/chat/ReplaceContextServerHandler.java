package org.goldius.netty.secured.chat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.ssl.SslContext;

public class ReplaceContextServerHandler extends SimpleChannelInboundHandler<String> {

    private final SslContext sslContext;

    public ReplaceContextServerHandler(SslContext sslContext) {
        this.sslContext = sslContext;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        if ("ssl reload".equalsIgnoreCase(msg)) {
            ((ReloadableSslContext)sslContext).reload();
        }
        ctx.fireChannelRead(msg);
    }

}
