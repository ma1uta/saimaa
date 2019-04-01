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

package io.github.ma1uta.saimaa.module.activitypub;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.ma1uta.saimaa.Bridge;
import io.github.ma1uta.saimaa.Module;
import io.github.ma1uta.saimaa.jaxrs.netty.JerseyServerInitializer;
import io.github.ma1uta.saimaa.jaxrs.netty.NettyHttpContainer;
import io.github.ma1uta.saimaa.netty.NettyBuilder;
import io.netty.channel.Channel;
import io.netty.handler.ssl.SslContext;
import org.jdbi.v3.core.Jdbi;

import java.net.URI;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * ActivityPub module.
 */
public class ActivityPubModule implements Module<ActivityPubConfig> {

    /**
     * Module name.
     */
    public static final String NAME = "activitypub";

    private ActivityPubConfig apConfig;
    private Jdbi jdbi;
    private ActivityPubApp app;
    private Channel channel;
    private SslContext sslContext;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void init(Map config, Bridge bridge) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        this.apConfig = mapper.convertValue(config, ActivityPubConfig.class);
        this.jdbi = bridge.getJdbi();

        Set<Object> resources = new HashSet<>();
        resources.add(new WebfingerResource(this.jdbi, this.apConfig));
        resources.add(new ActivityPubResource(this.jdbi, this.apConfig));
        this.app = new ActivityPubApp(resources);

        if (this.apConfig.getSsl() != null) {
            this.sslContext = this.apConfig.getSsl().createNettyContext();
        }
    }

    @Override
    public void run() {
        URI uri = URI.create(getConfig().getUrl());

        NettyHttpContainer container = new NettyHttpContainer(app);
        JerseyServerInitializer initializer = new JerseyServerInitializer(uri, sslContext, container);
        this.channel = NettyBuilder.createServer(uri.getHost(), NettyBuilder.getPort(uri), initializer,
            f -> container.getApplicationHandler().onShutdown(container));

    }

    @Override
    public ActivityPubConfig getConfig() {
        return this.apConfig;
    }

    @Override
    public void close() throws Exception {
        channel.close().sync();
    }
}
