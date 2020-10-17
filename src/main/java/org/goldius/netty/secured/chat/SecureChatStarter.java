package org.goldius.netty.secured.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SecureChatStarter {

    final static Logger logger = LoggerFactory.getLogger(SecureChatStarter.class);

    public static void main(String... args) throws IOException, InterruptedException {

        if (args.length == 1 && "client".equalsIgnoreCase(args[0])) {
            logger.info("Starting client!");
            SecureChatClient client = new SecureChatClient();
            client.start();
        } else {
            logger.info("Starting server!");
            SecureChatServer server = new SecureChatServer();
            server.start();
        }
    }
}
