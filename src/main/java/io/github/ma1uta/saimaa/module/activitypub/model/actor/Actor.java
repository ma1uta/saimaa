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

package io.github.ma1uta.saimaa.module.activitypub.model.actor;

import io.github.ma1uta.saimaa.module.activitypub.model.core.Collection;
import io.github.ma1uta.saimaa.module.activitypub.model.core.Object;
import io.github.ma1uta.saimaa.module.activitypub.model.core.OrderedCollection;

import java.util.Map;

/**
 * Common actor fields.
 */
public abstract class Actor extends Object {

    private OrderedCollection inbox;

    private OrderedCollection outbox;

    private Collection following;

    private Collection followers;

    private Collection liked;

    private Collection streams;

    private String preferredUsername;

    private Map<String, Endpoint> endpoints;

    public OrderedCollection getInbox() {
        return inbox;
    }

    public void setInbox(OrderedCollection inbox) {
        this.inbox = inbox;
    }

    public OrderedCollection getOutbox() {
        return outbox;
    }

    public void setOutbox(OrderedCollection outbox) {
        this.outbox = outbox;
    }

    public Collection getFollowing() {
        return following;
    }

    public void setFollowing(Collection following) {
        this.following = following;
    }

    public Collection getFollowers() {
        return followers;
    }

    public void setFollowers(Collection followers) {
        this.followers = followers;
    }

    public Collection getLiked() {
        return liked;
    }

    public void setLiked(Collection liked) {
        this.liked = liked;
    }

    public Collection getStreams() {
        return streams;
    }

    public void setStreams(Collection streams) {
        this.streams = streams;
    }

    public String getPreferredUsername() {
        return preferredUsername;
    }

    public void setPreferredUsername(String preferredUsername) {
        this.preferredUsername = preferredUsername;
    }

    public Map<String, Endpoint> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(Map<String, Endpoint> endpoints) {
        this.endpoints = endpoints;
    }
}
