/*
 * Copyright sablintolya@gmai.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.ma1uta.saimaa.module.xmpp;

import io.github.ma1uta.saimaa.RouterFactory;
import io.github.ma1uta.saimaa.config.Cert;
import io.github.ma1uta.saimaa.module.Module;
import io.github.ma1uta.saimaa.module.matrix.MatrixModule;
import io.github.ma1uta.saimaa.module.xmpp.dialback.ServerDialback;
import io.github.ma1uta.saimaa.module.xmpp.netty.XmppServerInitializer;
import io.github.ma1uta.saimaa.netty.NettyBuilder;
import io.github.ma1uta.saimaa.router.xmpptomx.XmppMatrixDirectInviteRouter;
import io.netty.channel.Channel;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rocks.xmpp.addr.Jid;
import rocks.xmpp.core.net.ChannelEncryption;
import rocks.xmpp.core.net.ConnectionConfiguration;
import rocks.xmpp.core.stanza.model.Stanza;
import rocks.xmpp.core.stream.model.StreamElement;

import java.net.InetSocketAddress;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.inject.Inject;
import javax.net.ssl.SSLContext;

/**
 * XMPP server with S2S support.
 */
public class XmppModule implements Module<XmppConfig> {

    /**
     * Module name.
     */
    public static final String NAME = "xmpp";

    private static final Logger LOGGER = LoggerFactory.getLogger(XmppModule.class);

    /**
     * Default XMPP S2S port.
     */
    static final int DEFAULT_S2S_PORT = 5269;

    @Inject
    private Jdbi jdbi;
    @Inject
    private XmppConfig config;
    @Inject
    private RouterFactory routerFactory;

    private final Map<InetSocketAddress, Set<IncomingSession>> incoming = new ConcurrentHashMap<>();
    private final Map<String, Set<OutgoingSession>> outgoing = new ConcurrentHashMap<>();
    private ServerDialback dialback;
    private SSLContext sslContext;
    private Channel channel;
    private SrvNameResolver srvNameResolver;
    private final ConnectionConfiguration connectionConfig = new ConnectionConfiguration() {
        @Override
        public ChannelEncryption getChannelEncryption() {
            return sslContext != null ? ChannelEncryption.REQUIRED : ChannelEncryption.DISABLED;
        }

        @Override
        public SSLContext getSSLContext() {
            try {
                return sslContext != null ? sslContext : SSLContext.getDefault();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    };

    /**
     * Create new incoming session.
     *
     * @param session a new incoming session.
     */
    public void newIncomingSession(IncomingSession session) {
        LOGGER.debug("New incoming session.");
        getIncoming().computeIfAbsent(session.getConnection().getRemoteAddress(), k -> new HashSet<>()).add(session);
    }

    /**
     * Create new outgoing session.
     *
     * @param session a new outgoing session.
     */
    public void newOutgoingSession(OutgoingSession session) {
        LOGGER.debug("New outgoing session.");
        getOutgoing().computeIfAbsent(session.getDomain(), k -> new HashSet<>()).add(session);
    }

    public Map<InetSocketAddress, Set<IncomingSession>> getIncoming() {
        return incoming;
    }

    public Map<String, Set<OutgoingSession>> getOutgoing() {
        return outgoing;
    }

    /**
     * Provide server connection configuration.
     *
     * @return server connection configuration.
     */
    public ConnectionConfiguration getConnectionConfiguration() {
        return connectionConfig;
    }

    public XmppConfig getConfig() {
        return config;
    }

    @Override
    public void close() throws Exception {
        List<Session> sessionsToRemove = new ArrayList<>();
        getIncoming().values().forEach(sessionsToRemove::addAll);
        getOutgoing().values().forEach(sessionsToRemove::addAll);
        for (Session session : sessionsToRemove) {
            try {
                session.close();
            } catch (Exception e) {
                LOGGER.error("Failed to close session.", e);
            }
        }
        this.channel.close().sync();
    }

    /**
     * Send outgoing message.
     *
     * @param message outgoing stanza.
     * @throws Exception if message sending was failed.
     */
    public void send(Stanza message) throws Exception {
        send(message.getTo(), message);
    }

    /**
     * Send outgoing message.
     *
     * @param to            remote address.
     * @param streamElement message.
     * @throws Exception if message sending was failed.
     */
    public void send(Jid to, StreamElement streamElement) throws Exception {
        String domain = to.getDomain();
        OutgoingSession session = getSession(domain);
        if (session == null) {
            session = new OutgoingSession(this, to.getDomain(), true);
            newOutgoingSession(session);
        }
        session.send(streamElement);
    }

    private OutgoingSession getSession(String domain) {
        Set<OutgoingSession> sessions = getOutgoing().get(domain);
        return sessions != null && !sessions.isEmpty() ? sessions.iterator().next() : null;
    }

    private void initDnsResolver() {
        this.srvNameResolver = new SrvNameResolver();
    }

    private void initSSL(XmppConfig config) throws Exception {
        Cert cert = config.getSsl();
        if (cert != null) {
            this.sslContext = cert.createJavaContext();
        }
    }

    private void initRouters() {
        routerFactory.addModuleRouter(XmppModule.NAME, MatrixModule.NAME, new XmppMatrixDirectInviteRouter());
    }

    /**
     * Provides ServerDialback mechanism.
     *
     * @return dialback service.
     */
    public ServerDialback dialback() {
        return dialback;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void run() throws Exception {
        this.dialback = new ServerDialback(this);
        initSSL(config);
        initRouters();
        initDnsResolver();

        this.channel = NettyBuilder.createServer(config.getDomain(), config.getPort(), new XmppServerInitializer(this), null);
    }

    /**
     * Remove closed session.
     *
     * @param session session to remove.
     */
    public void remove(Session session) {
        if (session instanceof OutgoingSession) {
            if (!((OutgoingSession) session).isDialbackEnabled()) {
                return;
            }
            Set<OutgoingSession> sessions = getOutgoing().get(session.getDomain());
            if (sessions != null) {
                sessions.remove(session);
            }
        } else if (session instanceof IncomingSession) {
            Set<IncomingSession> sessions = getIncoming().get(session.getConnection().getRemoteAddress());
            if (sessions != null) {
                sessions.remove(session);
            }
        } else {
            LOGGER.error("Unknown session.");
        }
    }

    /**
     * Process incoming stanzas.
     *
     * @param stanza incoming stanzas.
     */
    public void process(Stanza stanza) {
        routerFactory.process(XmppModule.NAME, stanza);
    }

    public SrvNameResolver getSrvNameResolver() {
        return srvNameResolver;
    }
}
