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

package io.github.ma1uta.saimaa.router;

import io.github.ma1uta.saimaa.router.mxtoxmpp.MatrixXmppDirectInviteRouter;
import io.github.ma1uta.saimaa.router.mxtoxmpp.MatrixXmppMessageRouter;
import io.github.ma1uta.saimaa.router.xmpptomx.XmppMatrixDirectInviteRouter;
import org.osgl.inject.loader.TypedElementLoader;

import java.util.Arrays;
import java.util.List;

/**
 * Router module.
 */
public class RouterLoader extends TypedElementLoader<AbstractRouter> {

    @Override
    protected List<Class<? extends AbstractRouter>> load(Class<AbstractRouter> type, boolean loadNonPublic, boolean loadAbstract,
                                                         boolean loadRoot) {
        return Arrays.asList(
            // Matrix -> XMPP
            MatrixXmppDirectInviteRouter.class,
            MatrixXmppMessageRouter.class,

            // XMPP -> Matrix
            XmppMatrixDirectInviteRouter.class
        );
    }
}
