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

package io.github.ma1uta.saimaa.module.xmpp.dialback;

import rocks.xmpp.core.stream.model.StreamElement;
import rocks.xmpp.core.stream.model.StreamFeature;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Dialback feature.
 */
@XmlRootElement(name = "dialback", namespace = "urn:xmpp:features:dialback")
@XmlType(factoryMethod = "create")
public class Dialback extends StreamFeature implements StreamElement {

    private DialbackError errors = new DialbackError();

    /**
     * The {@code <dialback/>} element.
     */
    public static final Dialback INSTANCE = new Dialback();

    public Dialback() {
    }

    @Override
    public String toString() {
        return "Dialback";
    }

    @XmlElement(name = "errors", namespace = "urn:xmpp:features:dialback")
    public DialbackError getErrors() {
        return errors;
    }

    public void setErrors(DialbackError errors) {
        this.errors = errors;
    }

    /**
     * Create the element.
     *
     * @return the {@code <dialback/>} element.
     */
    public static Dialback create() {
        return INSTANCE;
    }
}
