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

package io.github.ma1uta.saimaa.config;

import java.util.Map;

/**
 * Main appservice configuration.
 */
public class AppConfig {

    private Map<String, Map> module;

    private Map<String, Object> matrix;

    private DatabaseConfig database;

    public Map<String, Map> getModule() {
        return module;
    }

    public void setModule(Map<String, Map> module) {
        this.module = module;
    }

    public Map<String, Object> getMatrix() {
        return matrix;
    }

    public void setMatrix(Map<String, Object> matrix) {
        this.matrix = matrix;
    }

    public DatabaseConfig getDatabase() {
        return database;
    }

    public void setDatabase(DatabaseConfig database) {
        this.database = database;
    }
}
