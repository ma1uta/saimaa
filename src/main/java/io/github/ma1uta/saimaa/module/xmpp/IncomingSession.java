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

package io.github.ma1uta.saimaa.module.xmpp;

import io.github.ma1uta.saimaa.module.xmpp.dialback.ServerDialback;
import rocks.xmpp.addr.Jid;
import rocks.xmpp.core.XmppException;
import rocks.xmpp.core.stanza.model.Stanza;
import rocks.xmpp.core.stream.StreamNegotiationResult;
import rocks.xmpp.core.stream.model.StreamElement;
import rocks.xmpp.core.stream.model.StreamError;
import rocks.xmpp.core.stream.model.StreamErrorException;
import rocks.xmpp.core.stream.model.StreamFeatures;
import rocks.xmpp.core.stream.model.StreamHeader;
import rocks.xmpp.core.stream.model.errors.Condition;

import java.util.Locale;
import java.util.UUID;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;

/**
 * XMPP S2S incoming session.
 */
public class IncomingSession extends Session {

    public IncomingSession(XmppModule xmppModule) throws JAXBException {
        super(xmppModule);
    }

    @Override
    public boolean handleStream(Object streamElement) throws XmppException {
        if (isStanzaInvalid(streamElement)) {
            throw new StreamErrorException(new StreamError(Condition.IMPROPER_ADDRESSING));
        }
        if (streamElement instanceof StreamElement
            && getStreamFeaturesManager().handleElement((StreamElement) streamElement) == StreamNegotiationResult.RESTART) {
            return true;
        }
        if (streamElement instanceof StreamHeader) {
            StreamHeader streamHeader = (StreamHeader) streamElement;
            setDomain(streamHeader.getFrom().getDomain());
            String streamId = streamHeader.getId() != null ? streamHeader.getId() : UUID.randomUUID().toString();
            // send stream header response
            send(StreamHeader.responseServerToServer(
                Jid.of(getXmppModule().getConfig().getDomain()),
                streamHeader.getFrom(),
                streamId,
                Locale.getDefault(),
                new QName(ServerDialback.NAMESPACE, "", ServerDialback.PREFIX)
            ));
            // send supported features.
            send(new StreamFeatures(getStreamFeaturesManager().getStreamFeatures()));
            return false;
        }

        if (streamElement instanceof Stanza) {
            getXmppModule().process((Stanza) streamElement);
        }
        return false;
    }

    @Override
    public void send(StreamElement streamElement) {
        getExecutor().execute(() -> getConnection().send(streamElement));
    }

    @Override
    protected String direction() {
        return "income";
    }
}
