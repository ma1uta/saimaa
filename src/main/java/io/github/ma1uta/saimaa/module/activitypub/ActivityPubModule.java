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

package io.github.ma1uta.saimaa.module.activitypub;

import io.github.ma1uta.saimaa.Bridge;
import io.github.ma1uta.saimaa.Module;

import java.util.Map;

/**
 * ActivityPub module.
 */
public class ActivityPubModule implements Module {

    /**
     * Module name.
     */
    public static final String NAME = "activitypub";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void init(Map config, Bridge bridge) throws Exception {

    }

    @Override
    public void run() {

    }

    @Override
    public void close() throws Exception {

    }
}
