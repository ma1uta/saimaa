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

import io.github.ma1uta.saimaa.module.activitypub.db.Actor;
import io.github.ma1uta.saimaa.module.activitypub.model.core.Object;

/**
 * ActivityPub incoming message.
 */
public class IncomingMessage {

    private Actor actor;

    private Object message;

    public IncomingMessage() {
    }

    public IncomingMessage(Actor actor, Object message) {
        this.actor = actor;
        this.message = message;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}
