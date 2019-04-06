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

package io.github.ma1uta.saimaa.module.activitypub.db;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ACtor row mapper.
 */
public class ActorRowMapper implements RowMapper<Actor> {

    @Override
    public Actor map(ResultSet rs, StatementContext ctx) throws SQLException {
        Actor actor = new Actor();
        actor.setMxid(rs.getString("mxid"));
        actor.setRoomId(rs.getString("room_id"));
        actor.setGroup(rs.getBoolean("group"));
        return actor;
    }
}
