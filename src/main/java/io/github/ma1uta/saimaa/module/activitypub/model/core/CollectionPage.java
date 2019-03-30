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

package io.github.ma1uta.saimaa.module.activitypub.model.core;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.ma1uta.saimaa.module.activitypub.helpers.ObjectDeserializer;

/**
 * <a href="https://www.w3.org/TR/activitystreams-vocabulary/#dfn-collectionpage">Collection page</a>.
 */
public class CollectionPage extends Collection {

    /**
     * Type.
     */
    public static final String TYPE = "CollectionPage";

    @JsonDeserialize(using = ObjectDeserializer.class)
    private java.lang.Object partOf;

    @JsonDeserialize(using = ObjectDeserializer.class)
    private java.lang.Object next;

    @JsonDeserialize(using = ObjectDeserializer.class)
    private java.lang.Object prev;

    @Override
    public String getType() {
        return TYPE;
    }

    public java.lang.Object getPartOf() {
        return partOf;
    }

    public void setPartOf(java.lang.Object partOf) {
        this.partOf = partOf;
    }

    public java.lang.Object getNext() {
        return next;
    }

    public void setNext(java.lang.Object next) {
        this.next = next;
    }

    public java.lang.Object getPrev() {
        return prev;
    }

    public void setPrev(java.lang.Object prev) {
        this.prev = prev;
    }
}
