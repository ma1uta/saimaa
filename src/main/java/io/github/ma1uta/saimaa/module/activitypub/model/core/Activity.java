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
 * <a href="https://www.w3.org/TR/activitystreams-vocabulary/#dfn-activity">Activity</a>.
 */
public class Activity extends Object {

    /**
     * Type.
     */
    public static final String TYPE = "Activity";

    @JsonDeserialize(using = ObjectDeserializer.class)
    private List<?> actor;

    private Object object;

    @JsonDeserialize(using = ObjectDeserializer.class)
    private java.lang.Object target;

    @JsonDeserialize(using = ObjectDeserializer.class)
    private java.lang.Object result;

    @JsonDeserialize(using = ObjectDeserializer.class)
    private java.lang.Object origin;

    @JsonDeserialize(using = ObjectDeserializer.class)
    private List<?> instrument;

    @Override
    public String getType() {
        return TYPE;
    }

    public List<?> getActor() {
        return actor;
    }

    public void setActor(List<?> actor) {
        this.actor = actor;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public java.lang.Object getTarget() {
        return target;
    }

    public void setTarget(java.lang.Object target) {
        this.target = target;
    }

    public java.lang.Object getResult() {
        return result;
    }

    public void setResult(java.lang.Object result) {
        this.result = result;
    }

    public java.lang.Object getOrigin() {
        return origin;
    }

    public void setOrigin(java.lang.Object origin) {
        this.origin = origin;
    }

    public List<?> getInstrument() {
        return instrument;
    }

    void setInstrument(List<?> instrument) {
        this.instrument = instrument;
    }
}
