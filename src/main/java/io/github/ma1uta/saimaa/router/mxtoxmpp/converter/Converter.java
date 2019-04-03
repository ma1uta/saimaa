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

package io.github.ma1uta.saimaa.router.mxtoxmpp.converter;

import io.github.ma1uta.matrix.event.RoomMessage;
import io.github.ma1uta.matrix.event.content.RoomMessageContent;
import rocks.xmpp.addr.Jid;
import rocks.xmpp.core.stanza.model.Message;

import java.util.function.BiFunction;

/**
 * Converter class.
 *
 * @param <S> target xmpp stanza class.
 * @param <M> source matrix message class.
 */
public interface Converter<M extends RoomMessageContent, S extends Message> extends BiFunction<Jid, RoomMessage<?>, S> {

    /**
     * Provide source matrix message class.
     *
     * @return message class.
     */
    Class<M> getSourceClass();
}
