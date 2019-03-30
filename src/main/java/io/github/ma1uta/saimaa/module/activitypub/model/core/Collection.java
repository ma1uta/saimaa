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

import java.util.List;

/**
 * <a href="https://www.w3.org/TR/activitystreams-vocabulary/#dfn-collection">Collection</a>.
 */
public class Collection extends Contextable {

    /**
     * Type.
     */
    public static final String TYPE = "Collection";

    private Long totalItems;

    @JsonDeserialize(using = ObjectDeserializer.class)
    private java.lang.Object current;

    @JsonDeserialize(using = ObjectDeserializer.class)
    private java.lang.Object first;

    @JsonDeserialize(using = ObjectDeserializer.class)
    private java.lang.Object last;

    @JsonDeserialize(using = ObjectDeserializer.class)
    private List<java.lang.Object> items;

    @Override
    public String getType() {
        return TYPE;
    }

    public Long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Long totalItems) {
        this.totalItems = totalItems;
    }

    public java.lang.Object getCurrent() {
        return current;
    }

    public void setCurrent(java.lang.Object current) {
        this.current = current;
    }

    public java.lang.Object getFirst() {
        return first;
    }

    public void setFirst(java.lang.Object first) {
        this.first = first;
    }

    public java.lang.Object getLast() {
        return last;
    }

    public void setLast(java.lang.Object last) {
        this.last = last;
    }

    public List<java.lang.Object> getItems() {
        return items;
    }

    public void setItems(List<java.lang.Object> items) {
        this.items = items;
    }
}
