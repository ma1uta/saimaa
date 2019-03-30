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

import io.github.ma1uta.matrix.ErrorResponse;
import io.github.ma1uta.matrix.impl.exception.MatrixException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 * Security filter.
 */
@Provider
public class SecurityContextFilter implements ContainerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityContextFilter.class);

    private final String accessToken;

    public SecurityContextFilter(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String accessToken = requestContext.getUriInfo().getQueryParameters().getFirst("access_token");
        if (accessToken == null || accessToken.trim().isEmpty()) {
            LOGGER.error("Missing access token.");
            throw new MatrixException("_UNAUTHORIZED", "", Response.Status.UNAUTHORIZED.getStatusCode());
        }

        if (!getAccessToken().equals(accessToken)) {
            LOGGER.error("Wrong access token.");
            throw new MatrixException(ErrorResponse.Code.M_FORBIDDEN, "", Response.Status.FORBIDDEN.getStatusCode());
        }
    }

    public String getAccessToken() {
        return accessToken;
    }
}
