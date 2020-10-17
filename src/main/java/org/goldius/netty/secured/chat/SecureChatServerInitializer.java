package org.goldius.netty.secured.chat;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslContext;

public class SecureChatServerInitializer extends ChannelInitializer<SocketChannel> {

    private final SslContext sslCtx;

    public SecureChatServerInitializer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    protected void initChannel(SocketChannel ch) throws Exception {

        ChannelPipeline pipeline = ch.pipeline();

        // Add SSL handler first to encrypt and decrypt anything
        // In this example, we use a bogus certificate in teh server side
        // and accept any invalid certificate on the client side.
        pipeline.addLast(sslCtx.newHandler(ch.alloc()));

        // On top of the SSL handler add the text line codec
        pipeline.addLast(new DelimiterBasedFrameDecoder(8092, Delimiters.lineDelimiter()));
        pipeline.addLast(new StringDecoder());
        pipeline.addLast(new StringEncoder());

        // and then the business logic
        pipeline.addLast(new SecureChatServerHandler());

    }
}
