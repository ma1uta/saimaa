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

package io.github.ma1uta.saimaa.router.mxtoxmpp;

import io.github.ma1uta.matrix.event.RoomMessage;
import io.github.ma1uta.matrix.event.content.RoomMessageContent;
import io.github.ma1uta.saimaa.db.xmpp.DirectRoom;
import io.github.ma1uta.saimaa.db.xmpp.RoomDao;
import io.github.ma1uta.saimaa.router.mxtoxmpp.converter.Converter;
import io.github.ma1uta.saimaa.router.mxtoxmpp.converter.ConverterLoader;
import org.osgl.inject.annotation.LoadCollection;
import org.osgl.inject.annotation.MapKey;
import rocks.xmpp.addr.Jid;
import rocks.xmpp.core.stanza.model.ExtensibleStanza;
import rocks.xmpp.core.stanza.model.server.ServerMessage;

import java.util.Map;

/**
 * Matrix to XMPP message router.
 */
public class MatrixXmppMessageRouter extends MatrixXmppRouter {

    @MapKey("getSourceClass")
    @LoadCollection(ConverterLoader.class)
    private Map<Class<? extends RoomMessageContent>, Converter<RoomMessageContent, ? extends ExtensibleStanza>> converters;

    /**
     * Provides message converters.
     *
     * @param key message class.
     * @return converter.
     */
    private Converter<RoomMessageContent, ? extends ExtensibleStanza> getConverter(Class<? extends RoomMessageContent> key) {
        return converters.get(key);
    }

    @Override
    public boolean canProcess(Object message) {
        return message instanceof RoomMessage;
    }

    @Override
    public boolean process(Object message) {
        RoomMessage<?> roomMessage = (RoomMessage<?>) message;
        Converter<?, ?> converter = getConverter(roomMessage.getContent().getClass());
        if (converter == null) {
            return false;
        }

        return getJdbi().inTransaction(h -> {
            RoomDao roomDao = h.attach(RoomDao.class);
            DirectRoom room = roomDao.findDirectRoomByUserId(roomMessage.getSender());
            if (room == null) {
                return false;
            }

            ServerMessage xmppMessage = ServerMessage.from(converter.apply(room.getXmppJid(), roomMessage));
            xmppMessage.setFrom(Jid.of(getIdHelper().extractJidFromMxid(roomMessage.getSender())));

            try {
                getXmppModule().send(xmppMessage);
                return true;
            } catch (Exception e) {
                LOGGER.error("Unable to send message.", e);
                return false;
            }
        });
    }
}
