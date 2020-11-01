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

/**
 *
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 * This code is not thread safe and is only shown as an example of
 * design approach and not as production ready code.
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *
 * Author is not responsible for any issues caused from using this code or
 * designing the code based on this.
 *
 * Anyone can use this but the user is responsible if any issue occurs
 * based on using this code or design.
 *
 */
public class ReloadableSslContext extends SslContext {
    private SslContext sslContext;
    private SSLEngine sslEngine;

    public ReloadableSslContext() throws SSLException {
        Configuration cfg = ConfigFactory.create(Configuration.class);
        loadContext(cfg.serverCertPath(), cfg.serverKeyPath());
    }

    private void loadContext(String certPath, String keyPath) throws SSLException {
        InputStream keyCertChainIS = getClass().getResourceAsStream(certPath);
        InputStream keyIS =getClass().getResourceAsStream(keyPath);
        sslContext = SslContextBuilder.forServer(keyCertChainIS, keyIS).build();
    }

    public void reload(String certPath, String keyPath) throws SSLException {
        loadContext(certPath, keyPath);
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
