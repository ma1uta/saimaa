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

import io.github.ma1uta.saimaa.module.activitypub.model.core.Object;

import java.util.Map;

/**
 * Common actor fields.
 */
public abstract class Actor extends Object {

    private String inbox;

    private String outbox;

    private String following;

    private String followers;

    private String liked;

    private String streams;

    private String preferredUsername;

    private Map<String, Endpoint> endpoints;

    public String getInbox() {
        return inbox;
    }

    public void setInbox(String inbox) {
        this.inbox = inbox;
    }

    public String getOutbox() {
        return outbox;
    }

    public void setOutbox(String outbox) {
        this.outbox = outbox;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    public String getLiked() {
        return liked;
    }

    public void setLiked(String liked) {
        this.liked = liked;
    }

    public String getStreams() {
        return streams;
    }

    public void setStreams(String streams) {
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
