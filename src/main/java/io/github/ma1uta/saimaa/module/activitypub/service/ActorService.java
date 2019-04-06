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

import io.github.ma1uta.saimaa.db.activitypub.Actor;
import io.github.ma1uta.saimaa.db.activitypub.ActorDao;
import io.github.ma1uta.saimaa.module.activitypub.ActivityPubConfig;
import io.github.ma1uta.saimaa.module.activitypub.model.actor.Group;
import io.github.ma1uta.saimaa.module.activitypub.model.actor.Person;
import org.jdbi.v3.core.Jdbi;

import java.util.Collections;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;

/**
 * Actor service.
 */
public class ActorService {

    @Inject
    private Jdbi jdbi;
    @Inject
    private ActivityPubConfig config;

    /**
     * Get actor info.
     *
     * @param username actor's username.
     * @return actor info.
     * @throws NotFoundException            when the actor with specified username doesn't exist.
     * @throws InternalServerErrorException when failed to get actor info.
     */
    public io.github.ma1uta.saimaa.module.activitypub.model.actor.Actor actorInfo(String username) throws NotFoundException,
        InternalServerErrorException {
        try {
            Actor actor = jdbi.inTransaction(h -> h.attach(ActorDao.class).findByUsername(username));

            if (actor == null) {
                throw new NotFoundException();
            }

            io.github.ma1uta.saimaa.module.activitypub.model.actor.Actor response = actor.getGroup() == null || actor.getGroup()
                ? new Group()
                : new Person();

            String preparedUrl = config.getPreparedUrl();
            response.setProcessingContext("https://www.w3.org/ns/activitystreams");
            response.setId(preparedUrl + username);
            response.setPreferredUsername(username);
            response.setName(actor.getName());
            response.setIcon(Collections.singletonList(actor.getIcon()));
            response.setInbox(preparedUrl + username + "/inbox");
            response.setOutbox(preparedUrl + username + "/outbox");
            response.setFollowers(preparedUrl + username + "/followers");
            response.setFollowing(preparedUrl + username + "/following");

            return response;
        } catch (Exception e) {
            if (e instanceof NotFoundException) {
                throw e;
            } else {
                throw new InternalServerErrorException(e);
            }
        }
    }
}
