package org.goldius.netty.secured.chat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.ssl.SslHandshakeCompletionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.cert.X509Certificate;

public class PrintCertClientHandler extends ChannelInboundHandlerAdapter {

    final static Logger logger = LoggerFactory.getLogger(PrintCertClientHandler.class);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof SslHandshakeCompletionEvent) {
            SslHandler sslhandler = (SslHandler) ctx.channel().pipeline().get(SslHandler.class);
            X509Certificate[] certChain = sslhandler.engine().getSession().getPeerCertificateChain();

            logger.info("***********************************");
            logger.info("Subject DN: " + certChain[0].getSubjectDN().toString());
            logger.info("Not before: " + certChain[0].getNotBefore());
            logger.info("Not after: " + certChain[0].getNotAfter());
            logger.info("***********************************");
        }
        super.userEventTriggered(ctx, evt);
    }
}
