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

/**
 * Module.
 *
 * @param <C> config type.
 */
public interface Module<C> extends AutoCloseable {

    /**
     * Module name.
     *
     * @return module name.
     */
    String getName();

    /**
     * Run module.
     *
     * @throws Exception when module start was failed.
     */
    void run() throws Exception;

    /**
     * Retrieve module configuration.
     *
     * @return module configuration.
     */
    C getConfig();
}
