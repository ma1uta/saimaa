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

package io.github.ma1uta.saimaa.module.activitypub.model.actor;

/**
 * Additional endpoint.
 */
public class Endpoint {

    private String proxyUrl;

    private String oauthAuthorizationEndpoint;

    private String oauthTokenEndpoint;

    private String provideClientKey;

    private String signClientKey;

    private String sharedInbox;

    public String getProxyUrl() {
        return proxyUrl;
    }

    public void setProxyUrl(String proxyUrl) {
        this.proxyUrl = proxyUrl;
    }

    public String getOauthAuthorizationEndpoint() {
        return oauthAuthorizationEndpoint;
    }

    public void setOauthAuthorizationEndpoint(String oauthAuthorizationEndpoint) {
        this.oauthAuthorizationEndpoint = oauthAuthorizationEndpoint;
    }

    public String getOauthTokenEndpoint() {
        return oauthTokenEndpoint;
    }

    public void setOauthTokenEndpoint(String oauthTokenEndpoint) {
        this.oauthTokenEndpoint = oauthTokenEndpoint;
    }

    public String getProvideClientKey() {
        return provideClientKey;
    }

    public void setProvideClientKey(String provideClientKey) {
        this.provideClientKey = provideClientKey;
    }

    public String getSignClientKey() {
        return signClientKey;
    }

    public void setSignClientKey(String signClientKey) {
        this.signClientKey = signClientKey;
    }

    public String getSharedInbox() {
        return sharedInbox;
    }

    public void setSharedInbox(String sharedInbox) {
        this.sharedInbox = sharedInbox;
    }
}
