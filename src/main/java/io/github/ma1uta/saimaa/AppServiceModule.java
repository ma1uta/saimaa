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

package io.github.ma1uta.saimaa;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.ma1uta.matrix.client.AppServiceClient;
import io.github.ma1uta.matrix.client.factory.jaxrs.AppJaxRsRequestFactory;
import io.github.ma1uta.matrix.support.jackson.JacksonContextResolver;
import io.github.ma1uta.saimaa.config.AppConfig;
import io.github.ma1uta.saimaa.config.Cert;
import io.github.ma1uta.saimaa.config.DatabaseConfig;
import io.github.ma1uta.saimaa.module.activitypub.ActivityPubConfig;
import io.github.ma1uta.saimaa.module.activitypub.ActivityPubModule;
import io.github.ma1uta.saimaa.module.matrix.MatrixConfig;
import io.github.ma1uta.saimaa.module.xmpp.XmppConfig;
import io.github.ma1uta.saimaa.module.xmpp.XmppModule;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.postgres.PostgresPlugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Map;
import javax.net.ssl.SSLContext;
import javax.sql.DataSource;
import javax.ws.rs.client.ClientBuilder;

/**
 * Main appservice module for IoC.
 */
public class AppServiceModule extends org.osgl.inject.Module {

    private static final Logger LOGGER = LoggerFactory.getLogger(Loggers.LOGGER);

    private final AppConfig config;

    public AppServiceModule(AppConfig config) {
        this.config = config;
    }

    @Override
    protected void configure() {
        bind(AppConfig.class).to(this.config);

        DatabaseConfig databaseConfig = config.getDatabase();
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(databaseConfig.getDriverClass());
        hikariConfig.setJdbcUrl(databaseConfig.getUrl());
        hikariConfig.setUsername(databaseConfig.getUsername());
        hikariConfig.setPassword(databaseConfig.getPassword());
        databaseConfig.getProperties().forEach(hikariConfig::addDataSourceProperty);

        HikariDataSource dataSource = new HikariDataSource(hikariConfig);
        bind(DataSource.class).to(dataSource);

        Jdbi jdbi = Jdbi.create(dataSource);
        jdbi.installPlugin(new SqlObjectPlugin());
        jdbi.installPlugin(new PostgresPlugin());
        bind(Jdbi.class).to(jdbi);

        bind(RouterFactory.class).to(new RouterFactory());

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        MatrixConfig matrixConfig = mapper.convertValue(config.getMatrix(), MatrixConfig.class);

        bind(MatrixConfig.class).to(matrixConfig);

        for (Map.Entry<String, Map> moduleEntry : config.getModule().entrySet()) {
            switch (moduleEntry.getKey()) {
                case ActivityPubModule.NAME:
                    bind(ActivityPubConfig.class).to(mapper.convertValue(moduleEntry.getValue(), ActivityPubConfig.class));
                    break;
                case XmppModule.NAME:
                    bind(XmppConfig.class).to(mapper.convertValue(moduleEntry.getValue(), XmppConfig.class));
                    break;
                default:
                    // nothing
            }
        }

        try {
            ClientBuilder clientBuilder = ClientBuilder.newBuilder().register(new JacksonContextResolver());
            if (matrixConfig.isDisableSslValidation()) {
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, Cert.TRUST_ALL_CERTS, new SecureRandom());
                clientBuilder.sslContext(sslContext);
            }
            bind(AppServiceClient.class).to(new AppServiceClient.Builder()
                .requestFactory(new AppJaxRsRequestFactory(clientBuilder.build(), matrixConfig.getHomeserver()))
                .userId(matrixConfig.getMasterUserId())
                .accessToken(matrixConfig.getAsToken())
                .build());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            LOGGER.error("Unable to initialize Matrix client", e);
            throw new IllegalStateException(e);
        }
    }
}
