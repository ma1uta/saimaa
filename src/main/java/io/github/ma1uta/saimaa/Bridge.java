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

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.ma1uta.saimaa.config.AppConfig;
import io.github.ma1uta.saimaa.config.DatabaseConfig;
import io.github.ma1uta.saimaa.module.activitypub.ActivityPubModule;
import io.github.ma1uta.saimaa.module.matrix.MatrixModule;
import io.github.ma1uta.saimaa.module.xmpp.XmppModule;
import io.github.ma1uta.saimaa.router.mxtoxmpp.MatrixXmppDirectInviteRouter;
import io.github.ma1uta.saimaa.router.mxtoxmpp.MatrixXmppMessageRouter;
import io.github.ma1uta.saimaa.router.xmpptomx.XmppMatrixDirectInviteRouter;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.FileSystemResourceAccessor;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.postgres.PostgresPlugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Matrix-XMPP bridge.
 */
public class Bridge {

    private static final Logger LOGGER = LoggerFactory.getLogger(Loggers.LOGGER);

    private HikariDataSource dataSource;
    private Jdbi jdbi;
    private RouterFactory routerFactory;

    private LinkedHashMap<String, Module> modules = new LinkedHashMap<>();

    /**
     * Run bridge with the specified configuration.
     *
     * @param config bridge configuration.
     * @throws Exception when failed run the bridge.
     */
    public void run(AppConfig config) throws Exception {
        initDatabase(config.getDatabase());
        this.routerFactory = new RouterFactory();
        initModules(config);

        initRouters();

        modules.values().forEach(Module::run);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            modules.forEach((name, module) -> {
                try {
                    module.close();
                } catch (Exception e) {
                    LOGGER.error(String.format("Failed to stop module: '%s'", name), e);
                }
            });
            dataSource.close();
        }));
    }

    private void initModules(AppConfig config) throws Exception {
        Module module = new MatrixModule();
        module.init(config.getMatrix(), this);
        modules.put(MatrixModule.NAME, module);

        for (Map.Entry<String, Map> moduleItem : config.getModule().entrySet()) {
            String moduleName = moduleItem.getKey();
            switch (moduleName) {
                case XmppModule.NAME:
                    module = new XmppModule();
                    break;
                case ActivityPubModule.NAME:
                    module = new ActivityPubModule();
                    break;
                default:
                    throw new IllegalArgumentException(String.format("Unknown module: '%s'", moduleName));
            }
            module.init(moduleItem.getValue(), this);
            modules.put(moduleName, module);
        }
    }

    private void initRouters() {
        List<AbstractRouter> availableRouters = Arrays.asList(
            // Matrix -> XMPP
            new MatrixXmppDirectInviteRouter(),
            new MatrixXmppMessageRouter(),

            // XMPP -> Matrix
            new XmppMatrixDirectInviteRouter()
        );

        for (Map.Entry<String, Module> source : modules.entrySet()) {
            for (Map.Entry<String, Module> target : modules.entrySet()) {
                for (AbstractRouter router : availableRouters) {
                    if (router.canProcess(source.getKey(), target.getKey())) {
                        router.init(this, source.getValue(), target.getValue());
                        this.routerFactory.addModuleRouter(source.getKey(), target.getKey(), router);
                    }
                }
            }
        }
    }

    private void initDatabase(DatabaseConfig config) throws Exception {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(config.getDriverClass());
        hikariConfig.setJdbcUrl(config.getUrl());
        hikariConfig.setUsername(config.getUsername());
        hikariConfig.setPassword(config.getPassword());
        config.getProperties().forEach(hikariConfig::addDataSourceProperty);

        dataSource = new HikariDataSource(hikariConfig);
        jdbi = Jdbi.create(dataSource);
        jdbi.installPlugin(new SqlObjectPlugin());
        jdbi.installPlugin(new PostgresPlugin());
        updateSchema();
    }

    private void updateSchema() throws Exception {
        Database database = DatabaseFactory.getInstance()
            .findCorrectDatabaseImplementation(new JdbcConnection(dataSource.getConnection()));
        Liquibase liquibase = new Liquibase(getClass().getResource("/migrations.xml").getFile(), new FileSystemResourceAccessor(),
            database);
        liquibase.update(new Contexts(), new LabelExpression());
    }

    public Jdbi getJdbi() {
        return jdbi;
    }

    public RouterFactory getRouterFactory() {
        return routerFactory;
    }
}
