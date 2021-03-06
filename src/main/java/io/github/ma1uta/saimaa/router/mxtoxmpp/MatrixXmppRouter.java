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

package io.github.ma1uta.saimaa.router.mxtoxmpp;

import io.github.ma1uta.saimaa.module.helpers.IdHelper;
import io.github.ma1uta.saimaa.module.matrix.MatrixModule;
import io.github.ma1uta.saimaa.module.xmpp.XmppModule;
import io.github.ma1uta.saimaa.router.AbstractRouter;
import org.jdbi.v3.core.Jdbi;

import javax.inject.Inject;

/**
 * Process incoming matrix invite requests.
 */
public abstract class MatrixXmppRouter extends AbstractRouter {

    @Inject
    private Jdbi jdbi;
    @Inject
    private IdHelper idHelper;
    @Inject
    private MatrixModule matrixModule;
    @Inject
    private XmppModule xmppModule;

    @Override
    public boolean canProcess(String from, String to) {
        return MatrixModule.NAME.equals(from) && XmppModule.NAME.equals(to);
    }

    protected Jdbi getJdbi() {
        return jdbi;
    }

    protected IdHelper getIdHelper() {
        return idHelper;
    }

    protected MatrixModule getMatrixModule() {
        return matrixModule;
    }

    protected XmppModule getXmppModule() {
        return xmppModule;
    }
}
