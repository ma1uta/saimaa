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

package io.github.ma1uta.saimaa.router.xmpptomx;

import io.github.ma1uta.matrix.Id;
import io.github.ma1uta.matrix.client.AppServiceClient;
import io.github.ma1uta.matrix.client.model.account.RegisterRequest;
import io.github.ma1uta.matrix.client.model.room.CreateRoomRequest;
import io.github.ma1uta.saimaa.db.DirectRoom;
import io.github.ma1uta.saimaa.db.RoomDao;
import io.github.ma1uta.saimaa.db.UserDao;
import rocks.xmpp.addr.Jid;
import rocks.xmpp.core.stanza.model.Presence;

import java.util.Arrays;
import java.util.Optional;

/**
 * Process incoming xmpp invite presence.
 */
public class XmppMatrixDirectInviteRouter extends XmppMatrixRouter {

    @Override
    public boolean canProcess(Object message) {
        return message instanceof Presence;
    }

    @Override
    public boolean process(Object message) {
        Presence presence = (Presence) message;
        if (!Presence.Type.SUBSCRIBE.equals(presence.getType())) {
            return false;
        }

        Jid to = presence.getTo();

        if (to.getDomain().startsWith("conference.")) {
            return false;
        }

        String jid = to.toString();
        String target = getIdHelper().extractMxidFromJid(jid);
        String sender = getIdHelper().encodeJidToMxid(jid);

        return getJdbi().inTransaction(h -> {
            RoomDao roomDao = h.attach(RoomDao.class);
            DirectRoom room = roomDao.findDirectRoomByJid(sender);

            Optional<String> localPart = Id.localPart(target);
            if (!localPart.isPresent()) {
                return false;
            }
            String localpart = localPart.get();
            AppServiceClient matrixClient = getMatrixModule().getMatrixClient();

            UserDao userDao = h.attach(UserDao.class);
            if (userDao.exist(localpart) == 0) {
                RegisterRequest request = new RegisterRequest();
                request.setUsername(getMatrixModule().getConfig().getPrefix() + localpart);
                request.setInhibitLogin(false);
                matrixClient.account().register(request).join();
            }

            if (room == null) {
                CreateRoomRequest createRoomRequest = new CreateRoomRequest();
                createRoomRequest.setDirect(true);
                createRoomRequest.setInvite(Arrays.asList(sender, target));
                String roomId = matrixClient.userId(sender).room().create(createRoomRequest).join().getRoomId();

                room = roomDao.createDirectRoom(roomId, target, sender);
            }
            matrixClient.userId(sender).room().joinByIdOrAlias(room.getRoomId()).join();
            roomDao.updateXmppSubscription(room.getRoomId(), true);

            return true;
        });
    }
}
