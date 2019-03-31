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

import io.github.ma1uta.saimaa.AbstractRouter;
import io.github.ma1uta.saimaa.Bridge;
import io.github.ma1uta.saimaa.Module;
import io.github.ma1uta.saimaa.module.helpers.IdHelper;
import io.github.ma1uta.saimaa.module.matrix.MatrixModule;
import io.github.ma1uta.saimaa.module.xmpp.XmppModule;
import org.jdbi.v3.core.Jdbi;

/**
 * Process incoming matrix invite requests.
 */
public abstract class XmppMatrixRouter extends AbstractRouter {

    private Jdbi jdbi;
    private IdHelper idHelper;
    private MatrixModule matrixModule;
    private XmppModule xmppModule;

    @Override
    public boolean canProcess(String from, String to) {
        return XmppModule.NAME.equals(from) && MatrixModule.NAME.equals(to);
    }

    @Override
    public void init(Bridge bridge, Module source, Module target) {
        this.xmppModule = (XmppModule) source;
        this.matrixModule = (MatrixModule) target;
        this.idHelper = new IdHelper(matrixModule.getConfig(), xmppModule.getConfig());
        this.jdbi = bridge.getJdbi();
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
