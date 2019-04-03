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

package io.github.ma1uta.saimaa.module.helpers;

import io.github.ma1uta.matrix.Id;
import io.github.ma1uta.saimaa.Loggers;
import io.github.ma1uta.saimaa.module.matrix.MatrixConfig;
import io.github.ma1uta.saimaa.module.xmpp.XmppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import javax.inject.Inject;

/**
 * Id helper.
 */
public class IdHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(Loggers.LOGGER);

    @Inject
    private MatrixConfig matrixConfig;
    @Inject
    private XmppConfig xmppConfig;

    public MatrixConfig getMatrixConfig() {
        return matrixConfig;
    }

    public XmppConfig getXmppConfig() {
        return xmppConfig;
    }

    /**
     * Map MXID to JID.
     *
     * @param mxid MXID.
     * @return JID.
     */
    public String extractJidFromMxid(String mxid) {
        String prefix = getMatrixConfig().getPrefix();
        try {
            int delim = mxid.indexOf(":");
            String localpart = mxid.substring(1, delim);
            String prepMxid = localpart.startsWith(prefix) ? localpart.substring(prefix.length()) : localpart;
            String encodedJid = prepMxid.replaceAll("=", "%");
            return URLDecoder.decode(encodedJid, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("Your JRE doesn't have UTF-8 encoder", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Map JID to MXID.
     *
     * @param jid JID.
     * @return MXID.
     */
    public String extractMxidFromJid(String jid) {
        int localpartIndex = jid.indexOf("@");
        String localpart = localpartIndex == -1 ? jid : jid.substring(0, localpartIndex);

        try {
            return URLDecoder.decode(localpart, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("Your JRE doesn't have UTF-8 decoder", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Map JID to MXID.
     *
     * @param jid JID.
     * @return MXID.
     */
    public String encodeJidToMxid(String jid) {
        String encodedJid;
        try {
            encodedJid = URLEncoder.encode(jid, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("Your JRE doesn't have UTF-8 decoder", e);
            throw new RuntimeException(e);
        }
        String prepMxid = encodedJid.replaceAll("%", "=");
        MatrixConfig config = getMatrixConfig();
        return Id.Sigil.USER + config.getPrefix() + prepMxid + ":" + config.getHomeserver();
    }

    /**
     * Encode MXID to double-puppet JID.
     *
     * @param mxid MXID.
     * @return JID.
     */
    public String encodeMxidToJid(String mxid) {
        String mxidWithoutSigil = mxid.substring(1);
        return mxidWithoutSigil + "@" + getXmppConfig().getDomain();
    }
}
