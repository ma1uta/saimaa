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

package io.github.ma1uta.saimaa.module.activitypub.model.object;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.ma1uta.saimaa.module.activitypub.helpers.ObjectDeserializer;
import io.github.ma1uta.saimaa.module.activitypub.model.core.Object;

/**
 * <a href="https://www.w3.org/TR/activitystreams-vocabulary/#dfn-relationship">Relationship</a>.
 */
public class Relationship extends Object {

    /**
     * Type.
     */
    public static final String TYPE = "Relationship";

    @JsonDeserialize(using = ObjectDeserializer.class)
    private java.lang.Object subject;

    @JsonDeserialize(using = ObjectDeserializer.class)
    private java.lang.Object object;

    @JsonDeserialize(using = ObjectDeserializer.class)
    private java.lang.Object relationship;

    @Override
    public String getType() {
        return TYPE;
    }

    public java.lang.Object getSubject() {
        return subject;
    }

    public void setSubject(java.lang.Object subject) {
        this.subject = subject;
    }

    public java.lang.Object getObject() {
        return object;
    }

    public void setObject(java.lang.Object object) {
        this.object = object;
    }

    public java.lang.Object getRelationship() {
        return relationship;
    }

    public void setRelationship(java.lang.Object relationship) {
        this.relationship = relationship;
    }
}
