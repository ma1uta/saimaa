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
import io.github.ma1uta.saimaa.module.activitypub.service.ActorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(Loggers.AP_LOGGER);

    @Inject
    private ActorService actorService;

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

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(String.format("Find the actor: '%s'", username));
            }

            try {
                asyncResponse.resume(actorService.actorInfo(username));
            } catch (NotFoundException e) {
                LOGGER.error("Not found.", e);
                asyncResponse.resume(Response.status(Response.Status.NOT_FOUND).build());
            } catch (Exception e) {
                LOGGER.error(String.format("Failed to find the actor: '%s'", username), e);
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
     * @param sync          enable or not paging.
     * @param token         page token.
     * @param dir           pagination direction.
     * @param asyncResponse Asynchronous response.
     */
    @Path("/{username}/outbox")
    @GET
    public void getOutbox(
        @PathParam("username") String username,
        @QueryParam("sync") String sync,
        @QueryParam("token") String token,
        @QueryParam("dir") String dir,
        @Suspended AsyncResponse asyncResponse) {
        CompletableFuture.runAsync(() -> {
            try {
                asyncResponse.resume(actorService.outbox(username, sync, token, dir));
            } catch (NotFoundException e) {
                LOGGER.error(String.format("Actor '%s' doesn't exist", username), e);
                asyncResponse.resume(Response.status(Response.Status.NOT_FOUND).build());
            } catch (Exception e) {
                LOGGER.error(String.format("Failed to get outbox '%s' sync: '%s', token: '%s', dir: '%s'", username, sync, token, dir), e);
                asyncResponse.resume(Response.serverError().build());
            }
        });
    }

    /**
     * Followers.
     *
     * @param username      Username.
     * @param page          page number.
     * @param asyncResponse Asynchronous response.
     */
    @Path("/{username}/followers")
    @GET
    public void getFollowers(@PathParam("username") String username, @QueryParam("page") String page,
                             @Suspended AsyncResponse asyncResponse) {
        CompletableFuture.runAsync(() -> {
            try {
                asyncResponse.resume(actorService.getFollowers(username, page));
            } catch (NotFoundException e) {
                LOGGER.error(String.format("Actor '%s' doesn't exist", username), e);
                asyncResponse.resume(Response.status(Response.Status.NOT_FOUND).build());
            } catch (Exception e) {
                LOGGER.error(String.format("Failed to get followers '%s', page '%s'", username, page), e);
                asyncResponse.resume(Response.serverError().build());
            }
        });
    }

    /**
     * Following.
     *
     * @param username      Username.
     * @param page          page number.
     * @param asyncResponse Asynchronous response.
     */
    @Path("/{username}/following")
    @GET
    public void getFollowing(@PathParam("username") String username, @QueryParam("page") String page,
                             @Suspended AsyncResponse asyncResponse) {
        CompletableFuture.runAsync(() -> {
            try {
                asyncResponse.resume(actorService.getFollowing(username, page));
            } catch (NotFoundException e) {
                LOGGER.error(String.format("Actor '%s' doesn't exist", username), e);
                asyncResponse.resume(Response.status(Response.Status.NOT_FOUND).build());
            } catch (Exception e) {
                LOGGER.error(String.format("Failed to get followers '%s', page '%s'", username, page), e);
                asyncResponse.resume(Response.serverError().build());
            }
        });
    }
}
