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

package io.github.ma1uta.saimaa.router.mxtoxmpp.converter;

import org.osgl.Lang;
import org.osgl.inject.BeanSpec;
import org.osgl.inject.Genie;
import org.osgl.inject.loader.ElementLoaderBase;
import org.osgl.util.C;

import java.util.Map;

/**
 * Message converters.
 */
public class ConverterLoader extends ElementLoaderBase<Converter> {

    @Override
    public Iterable<Converter> load(Map<String, Object> options, BeanSpec container, Genie genie) {
        return C.newList(
            genie.get(TextConverter.class)
        );
    }

    @Override
    public Lang.Function<Converter, Boolean> filter(Map<String, Object> options, BeanSpec container) {
        return x -> true;
    }
}
