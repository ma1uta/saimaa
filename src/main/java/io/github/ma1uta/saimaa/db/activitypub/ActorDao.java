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

package io.github.ma1uta.saimaa.db.activitypub;

import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

/**
 * Actor DAO.
 */
public interface ActorDao {

    /**
     * Create the actor.
     *
     * @param username actor name.
     * @param roomId   actor room id.
     * @param mxid     owner of the actor.
     * @param group    public or private actor.
     * @param name     user name.
     * @param icon     icon url.
     */
    @SqlUpdate("insert into ap_actor(username, mxid, room_id, group, name, icon) values(:username, :mxid, :roomId, :group, :name, :icon)")
    void create(@Bind("username") String username, @Bind("roomId") String roomId, @Bind("mxid") String mxid, @Bind("group") Boolean group,
                @Bind("name") String name, @Bind("icon") String icon);

    /**
     * Update the actor.
     *
     * @param roomId actor room id.
     * @param mxid   owner of the actor.
     * @param group  public or private actor.
     * @param name   user name.
     * @param icon   icon url.
     */
    @SqlUpdate("update ap_actor set mxid = :mxid, group = :group, name = :name, icon = :icon where room_id = :roomId")
    void update(@Bind("roomId") String roomId, @Bind("mxid") String mxid, @Bind("group") Boolean group, @Bind("name") String name,
                @Bind("icon") String icon);

    /**
     * Find the actor by owner.
     *
     * @param mxid owner of the actor.
     * @return found actor.
     */
    @SqlQuery("select * from ap_actor where mxid = :mxid")
    @RegisterRowMapper(ActorRowMapper.class)
    Actor findByOwner(@Bind("mxid") String mxid);

    /**
     * Find the actor by room.
     *
     * @param roomId actor room id.
     * @return found actor.
     */
    @SqlQuery("select * from ap_actor where room_id = :roomId")
    @RegisterRowMapper(ActorRowMapper.class)
    Actor findByRoom(@Bind("roomId") String roomId);

    /**
     * Find the actor by username.
     *
     * @param username actor username.
     * @return found actor.
     */
    @SqlQuery("select * from ap_actor where username = :username")
    @RegisterRowMapper(ActorRowMapper.class)
    Actor findByUsername(@Bind("username") String username);

    /**
     * Get followers.
     *
     * @param roomId actor room.
     * @param offset how many followers skip.
     * @param limit  how many followers return.
     * @return followers.
     */
    @SqlQuery("select * from ap_followers where room_id = :roomId order by added limit :limit offset :offset")
    List<String> getFollowers(@Bind("roomId") String roomId, @Bind("offset") long offset, @Bind("limit") long limit);
}
