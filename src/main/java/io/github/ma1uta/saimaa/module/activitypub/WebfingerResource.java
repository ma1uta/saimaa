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

import io.github.ma1uta.saimaa.module.activitypub.service.WebfingerService;

import java.util.concurrent.CompletableFuture;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;

/**
 * AP Web resource.
 */
@Path("")
public class WebfingerResource {

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
        CompletableFuture.runAsync(() -> asyncResponse.resume(service.findResource(resource)));
    }
}
