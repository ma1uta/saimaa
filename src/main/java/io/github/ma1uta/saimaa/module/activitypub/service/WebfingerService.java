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

package io.github.ma1uta.saimaa.module.activitypub.service;

import io.github.ma1uta.saimaa.Loggers;
import io.github.ma1uta.saimaa.module.activitypub.ActivityPubConfig;
import io.github.ma1uta.saimaa.module.activitypub.db.Actor;
import io.github.ma1uta.saimaa.module.activitypub.db.ActorDao;
import io.github.ma1uta.saimaa.module.activitypub.model.webfinger.Link;
import io.github.ma1uta.saimaa.module.activitypub.model.webfinger.Resource;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;

/**
 * Webfinger service.
 */
public class WebfingerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(Loggers.AP_LOGGER);

    @Inject
    private Jdbi jdbi;
    @Inject
    private ActivityPubConfig config;

    /**
     * Find resource.
     *
     * @param resource actor name.
     * @return resource.
     * @throws BadRequestException          when missing the 'acct:' prefix.
     * @throws NotFoundException            when the specified resource doesn't exist.
     * @throws InternalServerErrorException when cannot find the specified resource.
     */
    public Object findResource(String resource) throws BadRequestException, NotFoundException, InternalServerErrorException {
        if (!resource.startsWith("acct:")) {
            throw new BadRequestException();
        }

        int delim = resource.indexOf(":");
        String actorName = resource.substring(delim + 1);

        try {
            Actor actor = jdbi.inTransaction(h -> h.attach(ActorDao.class).findByUsername(actorName));

            if (actor == null) {
                throw new NotFoundException();
            }

            Resource response = new Resource();
            response.setSubject(resource);
            Link link = new Link();
            link.setRel("self");
            link.setType("application/activity+json");
            link.setHref(config.getPreparedUrl() + actorName);
            response.setLinks(Collections.singletonList(link));

            return response;
        } catch (Exception e) {
            LOGGER.error(String.format("Unable to search resource: '%s'", resource), e);
            if (e instanceof NotFoundException) {
                throw e;
            } else {
                throw new InternalServerErrorException(e);
            }
        }
    }
}
