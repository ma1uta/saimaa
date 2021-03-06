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

import java.time.Instant;

/**
 * <a href="https://www.w3.org/TR/activitystreams-vocabulary/#dfn-tombstone">Tombstone</a>.
 */
public class Tombstone extends Object {

    /**
     * Type.
     */
    public static final String TYPE = "Tombstone";

    @JsonDeserialize(using = ObjectDeserializer.class)
    private java.lang.Object formerType;

    private Instant deleted;

    @Override
    public String getType() {
        return TYPE;
    }

    public java.lang.Object getFormerType() {
        return formerType;
    }

    public void setFormerType(java.lang.Object formerType) {
        this.formerType = formerType;
    }

    public Instant getDeleted() {
        return deleted;
    }

    public void setDeleted(Instant deleted) {
        this.deleted = deleted;
    }
}
