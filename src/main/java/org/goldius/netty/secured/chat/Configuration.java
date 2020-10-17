package org.goldius.netty.secured.chat;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:config/config.properties")
public interface Configuration extends Config {

    @Key("server.host")
    String serverHost();

    @Key("server.port")
    int port();

    @Key("server.cert.path")
    String serverCertPath();

    @Key("server.key.path")
    String serverKeyPath();

    @Key("client.cert.path")
    String clientCertPath();

    @Key("ssl.context.class")
    String sslContextClass();

    @Key("ssl.stream.reader")
    String sslStreamReader();

}
