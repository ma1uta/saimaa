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

/**
 * <a href="https://www.w3.org/TR/activitystreams-vocabulary/#dfn-orderedcollection">Ordered collection</a>.
 */
public class OrderedCollection extends Collection {

    /**
     * Type.
     */
    public static final String TYPE = "OrderedCollection";

    @Override
    public String getType() {
        return TYPE;
    }
}