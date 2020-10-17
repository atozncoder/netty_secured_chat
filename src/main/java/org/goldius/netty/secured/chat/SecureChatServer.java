package org.goldius.netty.secured.chat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import org.aeonbits.owner.ConfigFactory;

import javax.net.ssl.SSLException;
import java.io.InputStream;

public class SecureChatServer {

    private SslContext sslCtx;
    private ServerBootstrap bootstrap;

    public static void main(String... args) throws SSLException, InterruptedException {

        SecureChatServer server = new SecureChatServer();
        server.start();
    }

    public void start() throws InterruptedException, SSLException {

        Configuration cfg = ConfigFactory.create(Configuration.class);

        InputStream keyCertChainIS = getClass().getResourceAsStream(cfg.serverCertPath());
        InputStream keyIS = getClass().getResourceAsStream(cfg.serverKeyPath());
        sslCtx = SslContextBuilder.forServer(keyCertChainIS, keyIS).build();

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new SecureChatServerInitializer(sslCtx));

            bootstrap.bind(cfg.port()).sync().channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
