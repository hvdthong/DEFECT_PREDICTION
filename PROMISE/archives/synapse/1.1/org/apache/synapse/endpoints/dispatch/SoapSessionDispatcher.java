package org.apache.synapse.endpoints.dispatch;

import org.apache.synapse.endpoints.Endpoint;
import org.apache.synapse.MessageContext;
import org.apache.axiom.soap.SOAPHeader;
import org.apache.axiom.om.OMElement;

import javax.xml.namespace.QName;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

public class SoapSessionDispatcher implements Dispatcher {

    /**
     * Map to store session -> endpoint mappings. Synchronized map is used as this is accessed by
     * multiple threds (e.g. multiple clients different sessions).
     */
    private Map sessionMap = Collections.synchronizedMap(new HashMap());

    /**
     * Gives the endpoint based on the service group context ID of the request message.
     *
     * @param synCtx Request MessageContext, possibly containing a service group context ID.
     *
     * @return Endpoint associated with the soap session, if current message is a soap session
     * message and if current message is not the first message of the session. Returns null, if
     * an Endpoint could not be found for the session.
     */
    public Endpoint getEndpoint(MessageContext synCtx) {
        Endpoint endpoint = null;

        SOAPHeader header = synCtx.getEnvelope().getHeader();

        if(header != null) {
            OMElement sgcID = header.getFirstChildWithName(

            if(sgcID != null && sgcID.getText() != null) {

                Object e = sessionMap.get(sgcID.getText());

                if (e != null) {
                    endpoint = (Endpoint) e;
                }
            }
        }

        return endpoint;
    }

    /**
     * As this is a server initiated session, this method will only be called for response messages.
     * It extracts the service group context ID (if available) from the message and updates the
     * session (service group context ID) -> endpoint map.
     *
     * @param synCtx MessageContext of the response message.
     * @param endpoint Endpoint to associate with the session.
     */
    public void updateSession(MessageContext synCtx, Endpoint endpoint) {


        SOAPHeader header = synCtx.getEnvelope().getHeader();

        if(header != null) {
            OMElement replyTo = header.getFirstChildWithName

            if(replyTo != null) {
                OMElement referenceParameters = replyTo.getFirstChildWithName(new QName(

                if(referenceParameters != null) {
                    OMElement sgcID = referenceParameters.getFirstChildWithName(new QName(

                    synchronized(sessionMap) {
                        if(!sessionMap.containsKey(sgcID.getText())) {
                            sessionMap.put(sgcID.getText(), endpoint);
                        }
                    }
                }
            }
        }
    }

    public void unbind(MessageContext synCtx) {

        SOAPHeader header = synCtx.getEnvelope().getHeader();

        if(header != null) {
            OMElement sgcID = header.getFirstChildWithName(
            if(sgcID != null && sgcID.getText() != null) {
                sessionMap.remove(sgcID.getText());
            }
        }
    }

    /**
     * Soap session is initiated by the server. So this method always returns true.
     *
     * @return true
     */
    public boolean isServerInitiatedSession() {
        return true;
    }
}
