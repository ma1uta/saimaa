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

package io.github.ma1uta.saimaa.xmpp.netty;

import io.github.ma1uta.saimaa.xmpp.Session;
import io.github.ma1uta.saimaa.xmpp.XmppServer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

/**
 * Netty initializer.
 *
 * @param <C> socket type, client or server.
 * @param <S> session type, incoming or outgoing.
 */
public abstract class XmppNettyInitializer<C extends Channel, S extends Session> extends ChannelInitializer<C> {

    private final XmppServer server;

    protected XmppNettyInitializer(XmppServer server) {
        this.server = server;
    }

    public XmppServer getServer() {
        return server;
    }
}