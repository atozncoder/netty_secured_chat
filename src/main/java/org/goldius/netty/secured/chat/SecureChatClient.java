package org.goldius.netty.secured.chat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import org.aeonbits.owner.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class SecureChatClient {

    final static Logger logger = LoggerFactory.getLogger(SecureChatClient.class);

    public static void main(String... args) throws IOException, InterruptedException {

        SecureChatClient client = new SecureChatClient();
        client.start();
    }

    public void start() throws InterruptedException, IOException {

        Configuration cfg = ConfigFactory.create(Configuration.class);

        final SslContext sslCtx = SslContextBuilder.forClient()
                .trustManager(getInputStream())
                .build();


        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new SecureChatClientInitializer(sslCtx));

            // Start the connection attempt
            Channel ch = b.connect(cfg.serverHost(), cfg.port()).sync().channel();

            // Read commands from the stdin.
            ChannelFuture lastWriteFuture = null;
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            for (;;) {
                String line = in.readLine();
                if (line == null) {
                    break;
                }

                // Sends the received line to the server
                lastWriteFuture = ch.writeAndFlush(line + "\r\n");

                // If user types the 'bye' command, wait until server closes
                // the connection
                if ("bye".equals(line.toLowerCase())) {
                    ch.closeFuture().sync();
                    break;
                }
            }

            // Wait until all messages are flushed before closing the channel.
            if (lastWriteFuture != null) {
                lastWriteFuture.sync();
            }

        } finally {
            // The connection is closed automatically on shutdown.
            group.shutdownGracefully();
        }
    }

    private InputStream getInputStream() throws FileNotFoundException {
        Configuration cfg = ConfigFactory.create(Configuration.class);

        if(logger.isInfoEnabled()) {
            logger.info("**********Loading client cert************");
            logger.info("Stream reader: {}", cfg.sslStreamReader());
            logger.info("Cert path: {}", cfg.clientCertPath());
        }

        if ("classpath".equalsIgnoreCase(cfg.sslStreamReader())) {
            return getClass().getResourceAsStream(cfg.clientCertPath());
        } else {
            return new FileInputStream(cfg.clientCertPath());
        }
    }

}
