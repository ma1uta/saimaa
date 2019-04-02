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

import io.github.ma1uta.saimaa.router.AbstractRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Router factory.
 */
public class RouterFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouterFactory.class);

    /**
     * Routers. Source -&gt; Target -&gt; routers.
     */
    private Map<String, Map<String, Set<AbstractRouter>>> routers = new HashMap<>();

    /**
     * Add router.
     *
     * @param from   source module name.
     * @param to     target module name.
     * @param router new xmpp routers.
     */
    public void addModuleRouter(String from, String to, AbstractRouter router) {
        routers.computeIfAbsent(from, k -> new HashMap<>()).computeIfAbsent(to, k -> new HashSet<>()).add(router);
    }

    /**
     * Get routes of the specific source and target.
     *
     * @param from source module.
     * @return module routers.
     */
    public Map<String, Set<AbstractRouter>> getModuleRouters(String from) {
        return routers.getOrDefault(from, new HashMap<>());
    }

    /**
     * Process message.
     *
     * @param from    source module.
     * @param message message.
     */
    public void process(String from, Object message) {
        LOGGER.debug("Process message from '{}'", from);
        Map<String, Set<AbstractRouter>> routers = getModuleRouters(from);

        if (routers.isEmpty()) {
            LOGGER.warn("Empty routers.");
            return;
        }

        for (Map.Entry<String, Set<AbstractRouter>> target : routers.entrySet()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Process message to '{}'", target.getKey());
            }

            Set<AbstractRouter> targetRouters = target.getValue();
            if (targetRouters.isEmpty()) {
                LOGGER.warn("Empty routers.");
                continue;
            }

            for (AbstractRouter router : targetRouters) {
                LOGGER.debug("Router: {}", router.getClass());
                if (!router.canProcess(message)) {
                    LOGGER.debug("Skip processing.");
                    continue;
                }
                LOGGER.debug("Start processing.");
                try {
                    if (router.process(message)) {
                        LOGGER.debug("Stop processing.");
                        break;
                    }
                    LOGGER.debug("Next router.");
                } catch (Exception e) {
                    LOGGER.error("Failed process.", e);
                }
            }
        }
    }
}
