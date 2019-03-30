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

package io.github.ma1uta.saimaa.module.matrix;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.ma1uta.saimaa.ModuleConfig;
import io.github.ma1uta.saimaa.config.Cert;

/**
 * Matrix configuration.
 */
public class MatrixConfig implements ModuleConfig {

    private String url;

    @JsonProperty("as_token")
    private String asToken;

    @JsonProperty("hs_token")
    private String hsToken;

    @JsonProperty("master_user_id")
    private String masterUserId;

    private String prefix;

    private Cert ssl;

    private String homeserver;

    @JsonProperty("disable_ssl_validation")
    private boolean disableSslValidation;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAsToken() {
        return asToken;
    }

    public void setAsToken(String asToken) {
        this.asToken = asToken;
    }

    public String getHsToken() {
        return hsToken;
    }

    public void setHsToken(String hsToken) {
        this.hsToken = hsToken;
    }

    public String getMasterUserId() {
        return masterUserId;
    }

    public void setMasterUserId(String masterUserId) {
        this.masterUserId = masterUserId;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Cert getSsl() {
        return ssl;
    }

    public void setSsl(Cert ssl) {
        this.ssl = ssl;
    }

    public String getHomeserver() {
        return homeserver;
    }

    public void setHomeserver(String homeserver) {
        this.homeserver = homeserver;
    }

    public boolean isDisableSslValidation() {
        return disableSslValidation;
    }

    public void setDisableSslValidation(boolean disableSslValidation) {
        this.disableSslValidation = disableSslValidation;
    }
}
