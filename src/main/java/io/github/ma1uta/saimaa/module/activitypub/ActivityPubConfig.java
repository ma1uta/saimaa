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

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.ma1uta.saimaa.config.Cert;

/**
 * ActivityPub configuration.
 */
public class ActivityPubConfig {

    /**
     * Default page size in collections.
     */
    public static final long DEFAULT_PAGE_SIZE = 12L;

    private String url;

    private Cert ssl;

    private long pageSize = DEFAULT_PAGE_SIZE;

    @JsonIgnore
    private String preparedUrl;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Cert getSsl() {
        return ssl;
    }

    public void setSsl(Cert ssl) {
        this.ssl = ssl;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * Get prepared url (starts with schema and ends with slash).
     *
     * @return prepared url.
     */
    public String getPreparedUrl() {
        if (preparedUrl == null) {
            String url = getUrl();

            String urlWithSchema;
            if (url.startsWith("http://") || url.startsWith("https://")) {
                urlWithSchema = url;
            } else {
                urlWithSchema = (getSsl() != null ? "https://" : "http://") + url;
            }

            this.preparedUrl = urlWithSchema.endsWith("/") ? urlWithSchema : urlWithSchema + "/";
        }
        return preparedUrl;
    }
}
