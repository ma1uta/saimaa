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

package io.github.ma1uta.saimaa.module.activitypub.helpers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity object deserializer.
 */
public class ObjectDeserializer extends JsonDeserializer<Object> {

    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectCodec codec = p.getCodec();
        return deserialize(codec.readTree(p), codec);
    }

    private Object deserialize(JsonNode node, ObjectCodec codec) throws JsonProcessingException {
        if (node.isTextual()) {
            return node.asText();
        } else if (node.isArray()) {
            List<Object> array = new ArrayList<>(node.size());
            for (JsonNode item : node) {
                array.add(deserialize(item, codec));
            }
            return array;
        } else {
            return codec.treeToValue(node, io.github.ma1uta.saimaa.module.activitypub.model.core.Object.class);
        }
    }
}
