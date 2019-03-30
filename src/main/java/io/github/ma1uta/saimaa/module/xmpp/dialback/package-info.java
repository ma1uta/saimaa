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

/**
 * Server Dialback (https://xmpp.org/extensions/xep-0220.html).
 */

@XmlSchema(
    xmlns = {
        @XmlNs(prefix = ServerDialback.PREFIX, namespaceURI = ServerDialback.NAMESPACE)
    },
    namespace = StreamHeader.STREAM_NAMESPACE,
    elementFormDefault = XmlNsForm.QUALIFIED
)
package io.github.ma1uta.saimaa.module.xmpp.dialback;

import rocks.xmpp.core.stream.model.StreamHeader;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;