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

package io.github.ma1uta.saimaa.module;

import io.github.ma1uta.saimaa.module.activitypub.ActivityPubConfig;
import io.github.ma1uta.saimaa.module.activitypub.ActivityPubModule;
import io.github.ma1uta.saimaa.module.matrix.MatrixModule;
import io.github.ma1uta.saimaa.module.xmpp.XmppConfig;
import io.github.ma1uta.saimaa.module.xmpp.XmppModule;
import org.osgl.Lang;
import org.osgl.inject.BeanSpec;
import org.osgl.inject.Genie;
import org.osgl.inject.loader.ElementLoaderBase;
import org.osgl.util.C;

import java.util.Map;

/**
 * Module loader.
 */
public class ModuleLoader extends ElementLoaderBase<Module<?>> {

    @Override
    public Iterable<Module<?>> load(Map<String, Object> options, BeanSpec container, Genie genie) {
        C.List<Module<?>> modules = C.newList();
        modules.add(genie.get(MatrixModule.class));

        if (genie.hasProvider(XmppConfig.class)) {
            modules.add(genie.get(XmppModule.class));
        }

        if (genie.hasProvider(ActivityPubConfig.class)) {
            modules.add(genie.get(ActivityPubModule.class));
        }

        return modules;
    }

    @Override
    public Lang.Function<Module<?>, Boolean> filter(Map<String, Object> options, BeanSpec container) {
        return x -> true;
    }
}
