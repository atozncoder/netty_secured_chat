package org.goldius.netty.secured.chat;

import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.ssl.ApplicationProtocolNegotiator;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import org.aeonbits.owner.ConfigFactory;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSessionContext;
import java.io.InputStream;
import java.util.List;

public class ReloadableSslContext extends SslContext {
    private int state = -1;
    private SslContext sslContext;
    private SSLEngine sslEngine;

    public ReloadableSslContext() throws SSLException {
        loadContext();
    }

    private void loadContext() throws SSLException {

        Configuration cfg = ConfigFactory.create(Configuration.class);
        state = (state + 1) % 2;

        InputStream keyCertChainIS = null;
        InputStream keyIS = null;

        if (state == 0) {
            keyCertChainIS = getClass().getResourceAsStream(cfg.serverCertPath());
            keyIS =getClass().getResourceAsStream(cfg.serverKeyPath());
        } else {
            keyCertChainIS = getClass().getResourceAsStream(cfg.serverCertPath2());
            keyIS =getClass().getResourceAsStream(cfg.serverKeyPath2());
        }

        sslContext = SslContextBuilder.forServer(keyCertChainIS, keyIS).build();
    }

    public void reload() throws SSLException {
        loadContext();
    }

    @Override
    public boolean isClient() {
        return sslContext.isClient();
    }

    @Override
    public List<String> cipherSuites() {
        return sslContext.cipherSuites();
    }

    @Override
    public long sessionCacheSize() {
        return sslContext.sessionCacheSize();
    }

    @Override
    public long sessionTimeout() {
        return sslContext.sessionTimeout();
    }

    @Override
    public ApplicationProtocolNegotiator applicationProtocolNegotiator() {
        return sslContext.applicationProtocolNegotiator();
    }

    @Override
    public SSLEngine newEngine(ByteBufAllocator alloc) {
        this.sslEngine = sslContext.newEngine(alloc);
        return this.sslEngine;
    }

    @Override
    public SSLEngine newEngine(ByteBufAllocator alloc, String peerHost, int peerPort) {
        this.sslEngine =  sslContext.newEngine(alloc, peerHost, peerPort);
        return this.sslEngine;
    }

    @Override
    public SSLSessionContext sessionContext() {
        return sslContext.sessionContext();
    }

    public SSLEngine getSslEngine() {
        return sslEngine;
    }
}
