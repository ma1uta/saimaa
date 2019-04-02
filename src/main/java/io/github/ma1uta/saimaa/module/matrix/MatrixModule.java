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

package io.github.ma1uta.saimaa.module.matrix;

import io.github.ma1uta.matrix.Id;
import io.github.ma1uta.matrix.client.AppServiceClient;
import io.github.ma1uta.matrix.client.factory.jaxrs.AppJaxRsRequestFactory;
import io.github.ma1uta.matrix.client.model.account.RegisterRequest;
import io.github.ma1uta.matrix.support.jackson.JacksonContextResolver;
import io.github.ma1uta.saimaa.Loggers;
import io.github.ma1uta.saimaa.RouterFactory;
import io.github.ma1uta.saimaa.config.Cert;
import io.github.ma1uta.saimaa.db.matrix.UserDao;
import io.github.ma1uta.saimaa.jaxrs.netty.JerseyServerInitializer;
import io.github.ma1uta.saimaa.jaxrs.netty.NettyHttpContainer;
import io.github.ma1uta.saimaa.module.Module;
import io.github.ma1uta.saimaa.netty.NettyBuilder;
import io.netty.channel.Channel;
import io.netty.handler.ssl.SslContext;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.net.ssl.SSLContext;
import javax.ws.rs.client.ClientBuilder;

/**
 * All Matrix endpoints.
 */
public class MatrixModule implements Module<MatrixConfig> {

    /**
     * Module name.
     */
    public static final String NAME = "matrix";

    private MatrixApp matrixApp;
    private AppServiceClient matrixClient;
    private SslContext sslContext;
    private Channel channel;

    @Inject
    private MatrixConfig config;
    @Inject
    private Jdbi jdbi;
    @Inject
    private RouterFactory routerFactory;

    private void initMatrixClient() throws Exception {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder().register(new JacksonContextResolver());
        MatrixConfig config = getConfig();
        if (config.isDisableSslValidation()) {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, Cert.TRUST_ALL_CERTS, new SecureRandom());
            clientBuilder.sslContext(sslContext);
        }
        this.matrixClient = new AppServiceClient.Builder()
            .requestFactory(new AppJaxRsRequestFactory(clientBuilder.build(), config.getHomeserver()))
            .userId(config.getMasterUserId())
            .accessToken(config.getAsToken())
            .build();
    }

    private void initMasterBot() {
        try {
            this.jdbi.useTransaction(h -> {
                UserDao userDao = h.attach(UserDao.class);
                String masterId = getConfig().getMasterUserId();
                String localpart = Id.localPart(masterId).orElseThrow(() -> new RuntimeException("Wrong master id."));
                if (userDao.exist(localpart) == 0) {
                    RegisterRequest request = new RegisterRequest();
                    request.setUsername(localpart);
                    request.setInhibitLogin(false);
                    getMatrixClient().account().register(request).join();
                    userDao.create(localpart);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initRestAPI() {
        MatrixAppResource appResource = new MatrixAppResource(jdbi, routerFactory, matrixClient);
        Set<Object> resources = new HashSet<>();
        resources.add(appResource);
        resources.add(new LegacyMatrixAppResource(appResource));
        resources.add(new SecurityContextFilter(config.getHsToken()));
        resources.add(new MatrixExceptionHandler());
        resources.add(new JacksonContextResolver());
        if (LoggerFactory.getLogger(Loggers.REQUEST_LOGGER).isDebugEnabled()) {
            resources.add(new LoggingFilter());
        }
        this.matrixApp = new MatrixApp(resources);
    }

    private void initSSL() throws Exception {
        Cert cert = config.getSsl();
        if (cert != null) {
            sslContext = cert.createNettyContext();
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void run() throws Exception {
        initMatrixClient();
        initMasterBot();
        initRestAPI();
        initSSL();

        URI uri = URI.create(getConfig().getUrl());

        NettyHttpContainer container = new NettyHttpContainer(matrixApp);
        JerseyServerInitializer initializer = new JerseyServerInitializer(uri, sslContext, container);
        this.channel = NettyBuilder.createServer(uri.getHost(), NettyBuilder.getPort(uri), initializer,
            f -> container.getApplicationHandler().onShutdown(container));
    }

    @PreDestroy
    @Override
    public void close() throws Exception {
        channel.close().sync();
    }

    @Override
    public MatrixConfig getConfig() {
        return config;
    }

    public AppServiceClient getMatrixClient() {
        return matrixClient;
    }
}
