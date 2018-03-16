package org.apache.synapse.endpoints.dispatch;

import org.apache.synapse.endpoints.Endpoint;
import org.apache.synapse.MessageContext;
import org.apache.axiom.soap.SOAPHeader;
import org.apache.axiom.om.OMElement;

import javax.xml.namespace.QName;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

/**
 * This dispatcher is implemented to demonstrate a sample client session. It will detect sessions
 * request message. Therefore, above header has to be included in the request soap messages by the
 * client who wants to initiate and maintain a session.
 */
public class SimpleClientSessionDispatcher implements Dispatcher {

    /**
     * Map to store session -> endpoint mappings. Synchronized map is used as this is accessed by
     * multiple threds (e.g. multiple clients different sessions).
     */
    private Map sessionMap = Collections.synchronizedMap(new HashMap());

    public Endpoint getEndpoint(MessageContext synCtx) {

        SOAPHeader header = synCtx.getEnvelope().getHeader();

        if(header != null) {
            OMElement csID = header.getFirstChildWithName(
            if(csID != null && csID.getText() != null) {
                Object o = sessionMap.get(csID.getText());
                if (o != null) {
                    return (Endpoint) o;
                }
            }
        }

        return null;
    }

    public void updateSession(MessageContext synCtx, Endpoint endpoint) {

        SOAPHeader header = synCtx.getEnvelope().getHeader();

        if(header != null) {
            OMElement csID = header.getFirstChildWithName(
            if(csID != null && csID.getText() != null) {
                synchronized(sessionMap) {
                    if (!sessionMap.containsKey(csID.getText())) {
                        sessionMap.put(csID.getText(), endpoint);
                    }
                }
            }
        }
    }

    public void unbind(MessageContext synCtx) {

        SOAPHeader header = synCtx.getEnvelope().getHeader();

        if(header != null) {
            OMElement csID = header.getFirstChildWithName(
            if(csID != null && csID.getText() != null) {
                sessionMap.remove(csID.getText());
            }
        }
    }

    public boolean isServerInitiatedSession() {
        return false;
    }
}
