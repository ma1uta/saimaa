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

import com.zaxxer.hikari.HikariDataSource;
import io.github.ma1uta.saimaa.config.AppConfig;
import io.github.ma1uta.saimaa.module.Module;
import io.github.ma1uta.saimaa.module.ModuleLoader;
import io.github.ma1uta.saimaa.router.AbstractRouter;
import io.github.ma1uta.saimaa.router.RouterLoader;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.FileSystemResourceAccessor;
import org.osgl.inject.annotation.LoadCollection;
import org.osgl.inject.annotation.MapKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.inject.Inject;

/**
 * Matrix-XMPP bridge.
 */
public class Bridge {

    private static final Logger LOGGER = LoggerFactory.getLogger(Loggers.LOGGER);

    @Inject
    private AppConfig config;

    @Inject
    private HikariDataSource dataSource;

    @Inject
    private RouterFactory routerFactory;

    @MapKey("getName")
    @LoadCollection(ModuleLoader.class)
    private LinkedHashMap<String, Module> modules;

    @LoadCollection(RouterLoader.class)
    private Iterable<AbstractRouter> routers;

    /**
     * Run bridge with the specified configuration.
     *
     * @throws Exception when failed run the bridge.
     */
    public void run() throws Exception {
        updateSchema();
        initRouters();

        for (Module module : modules.values()) {
            module.run();
        }

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

    private void initRouters() {
        for (Map.Entry<String, Module> source : modules.entrySet()) {
            for (Map.Entry<String, Module> target : modules.entrySet()) {
                for (AbstractRouter router : routers) {
                    if (router.canProcess(source.getKey(), target.getKey())) {
                        this.routerFactory.addModuleRouter(source.getKey(), target.getKey(), router);
                    }
                }
            }
        }
    }

    private void updateSchema() throws Exception {
        Database database = DatabaseFactory.getInstance()
            .findCorrectDatabaseImplementation(new JdbcConnection(dataSource.getConnection()));
        Liquibase liquibase = new Liquibase(getClass().getResource("/migrations.xml").getFile(), new FileSystemResourceAccessor(),
            database);
        liquibase.update(new Contexts(), new LabelExpression());
    }
}
