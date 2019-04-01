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

import io.github.ma1uta.saimaa.Loggers;
import io.github.ma1uta.saimaa.db.activitypub.Actor;
import io.github.ma1uta.saimaa.db.activitypub.ActorDao;
import io.github.ma1uta.saimaa.module.activitypub.model.webfinger.Link;
import io.github.ma1uta.saimaa.module.activitypub.model.webfinger.Resource;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Response;

/**
 * AP Web resource.
 */
@Path("")
public class WebfingerResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(Loggers.LOGGER);

    private final Jdbi jdbi;
    private final ActivityPubConfig config;
    private String preparedUrl;

    public WebfingerResource(Jdbi jdbi, ActivityPubConfig config) {
        this.jdbi = jdbi;
        this.config = config;
        this.preparedUrl = addLastSplash(getFullApplicationUrl());
    }

    /**
     * Webfinger.
     *
     * @param resource      Actor for request.
     * @param asyncResponse Asynchronous response.
     */
    @Path("/.well-known/webfinger")
    @GET
    @Produces("application/json")
    public void finger(@QueryParam("resource") String resource, @Suspended AsyncResponse asyncResponse) {
        CompletableFuture.runAsync(() -> {
            if (!resource.startsWith("acct:")) {
                asyncResponse.resume(Response.status(Response.Status.BAD_REQUEST).build());
                return;
            }

            int delim = resource.indexOf(":");
            String actorName = resource.substring(delim + 1);

            try {
                Actor actor = jdbi.inTransaction(h -> h.attach(ActorDao.class).findByUsername(actorName));

                if (actor == null) {
                    asyncResponse.resume(Response.status(Response.Status.NOT_FOUND).build());
                    return;
                }

                Resource response = new Resource();
                response.setSubject(resource);
                Link link = new Link();
                link.setRel("self");
                link.setType("application/activity+json");
                link.setHref(preparedUrl + actorName);
                response.setLinks(Collections.singletonList(link));

                asyncResponse.resume(response);
            } catch (Exception e) {
                LOGGER.error(String.format("Unable to search resource: '%s'", resource), e);
                asyncResponse.resume(Response.serverError().build());
            }
        });
    }

    private String getFullApplicationUrl() {
        String url = config.getUrl();

        if (url.startsWith("http://") || url.startsWith("https://")) {
            return url;
        }

        return (config.getSsl() != null ? "https://" : "http://") + url;
    }

    private String addLastSplash(String url) {
        return url.endsWith("/") ? url : url + "/";
    }
}
