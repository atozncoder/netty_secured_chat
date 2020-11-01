package org.goldius.netty.secured.chat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.ssl.SslContext;
import org.aeonbits.owner.ConfigFactory;

public class ReplaceContextServerHandler extends SimpleChannelInboundHandler<String> {

    private boolean ctx1 = true;

    private final SslContext sslContext;

    public ReplaceContextServerHandler(SslContext sslContext) {
        this.sslContext = sslContext;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        if ("ssl reload".equalsIgnoreCase(msg)) {
            ctx1 = !ctx1;
            Configuration cfg = ConfigFactory.create(Configuration.class);
            if (ctx1) {
                ((ReloadableSslContext) sslContext).reload(cfg.serverCertPath(), cfg.serverKeyPath());
            } else {
                ((ReloadableSslContext) sslContext).reload(cfg.serverCertPath2(), cfg.serverKeyPath2());
            }
        }
        ctx.fireChannelRead(msg);
    }

}
