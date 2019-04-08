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

import io.github.ma1uta.matrix.Page;
import io.github.ma1uta.matrix.client.AppServiceClient;
import io.github.ma1uta.matrix.client.model.filter.FilterData;
import io.github.ma1uta.matrix.client.model.filter.RoomEventFilter;
import io.github.ma1uta.matrix.client.model.filter.RoomFilter;
import io.github.ma1uta.matrix.client.model.sync.Timeline;
import io.github.ma1uta.matrix.event.Event;
import io.github.ma1uta.saimaa.Loggers;
import io.github.ma1uta.saimaa.RouterFactory;
import io.github.ma1uta.saimaa.module.activitypub.ActivityPubConfig;
import io.github.ma1uta.saimaa.module.activitypub.ActivityPubModule;
import io.github.ma1uta.saimaa.module.activitypub.db.Actor;
import io.github.ma1uta.saimaa.module.activitypub.db.ActorDao;
import io.github.ma1uta.saimaa.module.activitypub.model.actor.Group;
import io.github.ma1uta.saimaa.module.activitypub.model.actor.Person;
import io.github.ma1uta.saimaa.module.activitypub.model.core.Object;
import io.github.ma1uta.saimaa.module.activitypub.model.core.OrderedCollection;
import io.github.ma1uta.saimaa.module.activitypub.model.core.OrderedCollectionPage;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;

/**
 * Actor service.
 */
public class ActorService {

    /**
     * Activity Streams NS.
     */
    public static final String ACTIVITY_STREAMS = "https://www.w3.org/ns/activitystreams";

    private static final Logger LOGGER = LoggerFactory.getLogger(Loggers.AP_LOGGER);

    @Inject
    private Jdbi jdbi;
    @Inject
    private ActivityPubConfig config;
    @Inject
    private RouterFactory routerFactory;
    @Inject
    private AppServiceClient mxClient;

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
            response.setProcessingContext(ACTIVITY_STREAMS);
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

    /**
     * Get actor followers.
     *
     * @param username      actor username.
     * @param pageNumberStr page number of the followers.
     * @return actor followers.
     * @throws InternalServerErrorException when failed to get followers.
     * @throws NotFoundException            when username doesn't exist.
     */
    public OrderedCollection getFollowers(String username, String pageNumberStr) throws InternalServerErrorException, NotFoundException {
        return collection(username, pageNumberStr, "followers", ActorDao::countFollowers, ActorDao::getFollowers);
    }

    /**
     * Get actor following.
     *
     * @param username      actor username.
     * @param pageNumberStr page number of the following.
     * @return actor followers.
     * @throws InternalServerErrorException when failed to get following.
     * @throws NotFoundException            when username doesn't exist.
     */
    public OrderedCollection getFollowing(String username, String pageNumberStr) throws InternalServerErrorException, NotFoundException {
        return collection(username, pageNumberStr, "following", ActorDao::countFollowing, ActorDao::getFollowing);
    }

    protected OrderedCollection collection(String username, String pageNumberStr, String type, CountMethod countMethod,
                                           GetMethod getMethod) throws InternalServerErrorException, NotFoundException {
        try {
            return jdbi.inTransaction(h -> {
                ActorDao dao = h.attach(ActorDao.class);

                Actor actor = dao.findByUsername(username);

                if (actor == null) {
                    throw new NotFoundException();
                }

                String preparedUrl = config.getPreparedUrl();

                OrderedCollectionPage page = new OrderedCollectionPage();
                page.setProcessingContext(ACTIVITY_STREAMS);
                long countFollowers = countMethod.count(dao, username);
                page.setTotalItems(countFollowers);
                if (pageNumberStr == null) {
                    page.setId(String.format("%s%s/%s", preparedUrl, username, type));
                    page.setFirst(String.format("%s%s/%s?page=1", preparedUrl, username, type));
                    return page;
                }

                long offset = 0;
                long pageNumber = 1;
                try {
                    pageNumber = Long.parseLong(pageNumberStr);
                    if (pageNumber < 1) {
                        pageNumber = 1;
                    }
                    offset = (pageNumber - 1) * config.getPageSize();
                } catch (NumberFormatException e) {
                    LOGGER.error(String.format("Wrong number: '%s'", pageNumberStr), e);
                }

                page.setId(String.format("%s%s/%s?page=%d", preparedUrl, username, type, pageNumber));
                if (offset + config.getPageSize() < countFollowers) {
                    page.setNext(String.format("%s%s/%s?page=%d", preparedUrl, username, type, pageNumber + 1));
                }
                if (pageNumber > 1) {
                    page.setPrev(String.format("%s%s/%s?page=%d", preparedUrl, username, type, pageNumber - 1));
                }
                page.setPartOf(String.format("%s%s/%s", preparedUrl, username, type));
                page.setOrderedItems(countFollowers > offset
                    ? getMethod.get(dao, username, offset, config.getPageSize())
                    : Collections.emptyList());

                return page;
            });
        } catch (Exception e) {
            LOGGER.error(String.format("Unable to find %s: '%s'", type, username));
            if (e instanceof NotFoundException) {
                throw e;
            } else {
                throw new InternalServerErrorException(e);
            }
        }
    }

    /**
     * Get outbox.
     *
     * @param username actor username.
     * @param sync     extract messages from history.
     * @param token    token to search messages.
     * @param dir      search direction.
     * @return outbox.
     * @throws NotFoundException            when specified actor wasn't find.
     * @throws InternalServerErrorException when cannot get actor outbox.
     */
    public OrderedCollection outbox(String username, String sync, String token, String dir) throws NotFoundException,
        InternalServerErrorException {
        try {
            return jdbi.inTransaction(h -> {
                ActorDao dao = h.attach(ActorDao.class);
                Actor actor = dao.findByUsername(username);

                if (actor == null) {
                    throw new NotFoundException();
                }

                String preparedUrl = config.getPreparedUrl();

                OrderedCollectionPage page = new OrderedCollectionPage();
                page.setProcessingContext(ACTIVITY_STREAMS);
                page.setTotalItems(actor.getTotalItems());

                if (sync == null) {
                    page.setId(String.format("%s%s/%s", preparedUrl, username, "outbox"));
                    page.setFirst(String.format("%s%s/%s?sync=true", preparedUrl, username, "outbox"));
                    page.setLast("");
                    return page;
                }

                String filter = getSyncFilter(actor, dao);

                String currentToken = token;
                String nextToken;
                String prevToken = null;
                if (currentToken == null) {
                    Timeline history = mxClient.sync().sync(filter, null, false, null, 0L).join().getRooms().getJoin()
                        .get(actor.getRoomId()).getTimeline();
                    currentToken = history.getPrevBatch();
                    nextToken = currentToken;
                } else {
                    Page<Event> eventPage = mxClient.event()
                        .messages(actor.getRoomId(), currentToken, null, dir, (int) config.getPageSize(), filter).join();
                    nextToken = "b".equals(dir) ? eventPage.getEnd() : eventPage.getStart();
                    prevToken = token;
                }

                page.setId(String.format("%s%s/%s?sync=true&token=%s&dir=%s", preparedUrl, username, "outbox", currentToken, dir));
                page.setNext(String.format("%s%s/%s?sync=true&token=%s&dir=%s", preparedUrl, username, "outbox", nextToken, dir));
                if (prevToken != null) {
                    page.setPrev(String.format("%s%s/%s?sync=true&token=%s&dir=%s", preparedUrl, username, "outbox", currentToken,
                        "b".equals(dir) ? "f" : "b"));
                }

                return page;
            });
        } catch (Exception e) {
            LOGGER.error(String.format("Unable to get outbox: '%s'", username), e);
            if (e instanceof NotFoundException) {
                throw e;
            } else {
                throw new InternalServerErrorException(e);
            }
        }
    }

    private String getSyncFilter(Actor actor, ActorDao dao) {
        String filter = actor.getSyncFilter();
        if (filter == null) {

            RoomFilter roomFilter = new RoomFilter();
            roomFilter.setRooms(Collections.singletonList(actor.getRoomId()));

            roomFilter.setEphemeral(excludeAllFilter());
            roomFilter.setState(excludeAllFilter());
            roomFilter.setAccountData(excludeAllFilter());

            FilterData filterData = new FilterData();
            filterData.setRoom(roomFilter);

            filter = mxClient.userId(actor.getMxid()).filter().uploadFilter(filterData).join().getFilterId();
            actor.setSyncFilter(filter);
            dao.updateFilter(actor.getUsername(), filter);
        }
        return filter;
    }

    private RoomEventFilter excludeAllFilter() {
        RoomEventFilter filter = new RoomEventFilter();
        filter.setNotTypes(Collections.singletonList("*"));
        return filter;
    }

    /**
     * Processing incoming message.
     *
     * @param username actor username.
     * @param message  incoming message.
     */
    public void processIncomingMessage(String username, Object message) {

        Actor actor = jdbi.inTransaction(h -> h.attach(ActorDao.class).findByUsername(username));

        if (actor == null) {
            throw new NotFoundException();
        }

        try {
            routerFactory.process(ActivityPubModule.NAME, new IncomingMessage(actor, message));
        } catch (Exception e) {
            LOGGER.error(String.format("Unable to process incoming message: '%s'", username), e);
            throw new InternalServerErrorException(e);
        }
    }

    @FunctionalInterface
    interface CountMethod {
        long count(ActorDao dao, String username);
    }

    @FunctionalInterface
    interface GetMethod {
        List<String> get(ActorDao dao, String username, long offset, long pageSize);
    }
}
