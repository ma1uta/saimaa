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
import io.github.ma1uta.saimaa.module.activitypub.service.WebfingerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(Loggers.AP_LOGGER);

    @Inject
    private WebfingerService service;

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

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(String.format("Find the resource: '%s'", resource));
            }

            try {
                asyncResponse.resume(service.findResource(resource));
            } catch (BadRequestException e) {
                LOGGER.error("Bad request.", e);
                asyncResponse.resume(Response.status(Response.Status.BAD_REQUEST).build());
            } catch (NotFoundException e) {
                LOGGER.error("Not found.", e);
                asyncResponse.resume(Response.status(Response.Status.NOT_FOUND).build());
            } catch (Exception e) {
                LOGGER.error(String.format("Failed to find the resource: '%s'", resource));
                asyncResponse.resume(Response.serverError().build());
            }
        });
    }
}
