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
import io.github.ma1uta.saimaa.module.activitypub.model.actor.Group;
import io.github.ma1uta.saimaa.module.activitypub.model.actor.Person;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Response;

/**
 * AP Web resource.
 */
@Path("")
@Consumes( {"application/ld+json; profile=\"https://www.w3.org/ns/activitystreams\"", "application/activity+json"})
@Produces( {"application/ld+json; profile=\"https://www.w3.org/ns/activitystreams\"", "application/activity+json"})
public class ActivityPubResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(Loggers.LOGGER);

    private final Jdbi jdbi;
    private final ActivityPubConfig config;
    private String preparedUrl;

    public ActivityPubResource(Jdbi jdbi, ActivityPubConfig config) {
        this.jdbi = jdbi;
        this.config = config;
        this.preparedUrl = addLastSplash(getFullApplicationUrl());
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

    /**
     * Actor info.
     *
     * @param username      Actor name.
     * @param asyncResponse Asynchronous response.
     */
    @Path("/{username}")
    @GET
    public void actor(@PathParam("username") String username, @Suspended AsyncResponse asyncResponse) {
        CompletableFuture.runAsync(() -> {
            try {
                Actor actor = jdbi.inTransaction(h -> h.attach(ActorDao.class).findByUsername(username));

                if (actor == null) {
                    asyncResponse.resume(Response.status(Response.Status.NOT_FOUND).build());
                    return;
                }

                io.github.ma1uta.saimaa.module.activitypub.model.actor.Actor response = actor.getGroup() == null || actor.getGroup()
                    ? new Group()
                    : new Person();

                response.setProcessingContext("https://www.w3.org/ns/activitystreams");
                response.setId(preparedUrl + username);
                response.setPreferredUsername(username);
                response.setName("//TODO");
                response.setIcon(Collections.singletonList("//TODO"));
                response.setInbox(preparedUrl + username + "/inbox");
                response.setOutbox(preparedUrl + username + "/outbox");
                response.setFollowers(preparedUrl + username + "/followers");
                response.setFollowing(preparedUrl + username + "/following");

                asyncResponse.resume(response);
            } catch (Exception e) {
                LOGGER.error(String.format("Unable to get actor info '%s'", username), e);
                asyncResponse.resume(Response.serverError().build());
            }
        });
    }

    /**
     * Inbox.
     *
     * @param username      Username.
     * @param asyncResponse Asynchronous response.
     */
    @Path("/{username}/inbox")
    @GET
    public void getInbox(@PathParam("username") String username, @Suspended AsyncResponse asyncResponse) {

    }

    /**
     * Inbox.
     *
     * @param username      Username.
     * @param asyncResponse Asynchronous response.
     */
    @Path("/{username}/inbox")
    @POST
    public void postInbox(@PathParam("username") String username, @Suspended AsyncResponse asyncResponse) {

    }

    /**
     * Outbox.
     *
     * @param username      Username.
     * @param asyncResponse Asynchronous response.
     */
    @Path("/{username}/outbox")
    @GET
    public void getOutbox(@PathParam("username") String username, @Suspended AsyncResponse asyncResponse) {

    }

    /**
     * Followers.
     *
     * @param username      Username.
     * @param asyncResponse Asynchronous response.
     */
    @Path("/{username}/followers")
    @GET
    public void getFollowers(@PathParam("username") String username, @Suspended AsyncResponse asyncResponse) {

    }

    /**
     * Following.
     *
     * @param username      Username.
     * @param asyncResponse Asynchronous response.
     */
    @Path("/{username}/following")
    @GET
    public void getFollowing(@PathParam("username") String username, @Suspended AsyncResponse asyncResponse) {

    }
}
