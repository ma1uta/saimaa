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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * All routers can send response to matrix and xmpp (i. e. normal processing from xmpp to matrix and send errors back to the xmpp).
 */
public abstract class AbstractRouter {

    protected static final Logger LOGGER = LoggerFactory.getLogger(Loggers.LOGGER);

    /**
     * Can router process this message.
     *
     * @param message message.
     * @return {@code true} if router can process this message, else {@code false}.
     */
    public abstract boolean canProcess(Object message);

    /**
     * Can router transform messages between two modules.
     *
     * @param from source module.
     * @param to   target module.
     * @return {@code true} if module process from source module to target.
     */
    public abstract boolean canProcess(String from, String to);

    /**
     * Process message.
     *
     * @param message message.
     * @return {@code true} if processing should be stop, else {@code false} if next router can process message.
     * @throws Exception when processing was failed.
     */
    public abstract boolean process(Object message) throws Exception;

    /**
     * Initialize router.
     *
     * @param bridge appservice.
     * @param source source module.
     * @param target target module.
     */
    public abstract void init(Bridge bridge, Module source, Module target);
}
