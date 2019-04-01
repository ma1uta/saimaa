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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.ma1uta.saimaa.module.activitypub.helpers.ProcessingContextDeserializer;

/**
 * Object with '@processingContext' field.
 */
public abstract class Contextable {

    @JsonProperty(value = "@context")
    @JsonDeserialize(using = ProcessingContextDeserializer.class)
    @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
    private java.lang.Object processingContext;

    private String id;

    /**
     * Object type.
     *
     * @return object type.
     */
    @JsonProperty(value = "type", access = JsonProperty.Access.READ_ONLY)
    public abstract String getType();

    public java.lang.Object getProcessingContext() {
        return processingContext;
    }

    public void setProcessingContext(java.lang.Object processingContext) {
        this.processingContext = processingContext;
    }

    /**
     * Global identifier.
     *
     * @return global identifier.
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
