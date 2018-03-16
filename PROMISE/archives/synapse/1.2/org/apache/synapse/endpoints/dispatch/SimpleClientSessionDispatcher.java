package org.apache.synapse.endpoints.dispatch;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAPHeader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.endpoints.Endpoint;

import javax.xml.namespace.QName;

/**
 * This dispatcher is implemented to demonstrate a sample client session. It will detect sessions
 * request message. Therefore, above header has to be included in the request soap messages by the
 * client who wants to initiate and maintain a session.
 */
public class SimpleClientSessionDispatcher implements Dispatcher {

    private static final Log log = LogFactory.getLog(SimpleClientSessionDispatcher.class);

    private static final QName CSID_QNAME

    public Endpoint getEndpoint(MessageContext synCtx, DispatcherContext dispatcherContext) {

        SOAPHeader header = synCtx.getEnvelope().getHeader();

        if (header != null) {
            OMElement sgcIDElm = header.getFirstChildWithName(CSID_QNAME);

            if (sgcIDElm != null) {
                String sgcID = sgcIDElm.getText();

                if (sgcID != null) {
                    log.debug("Using the client session id : '"
                            + sgcID + "' extracted from current message to retrieve endpoint");
                    Object o = dispatcherContext.getEndpoint(sgcID);

                    if (o != null && o instanceof Endpoint) {
                        return (Endpoint) o;
                    }
                } else if (log.isDebugEnabled()) {
                    log.debug("Couldn't find the client session id for the current message " +
                            "to retrieve endpoint");
                }
            } else if (log.isDebugEnabled()) {
                log.debug("Couldn't find a SOAP header with the QName " + CSID_QNAME +
                        " for the current message to retrieve the endpoint");
            }
        }

        return null;
    }

    public void updateSession(MessageContext synCtx, DispatcherContext dispatcherContext,
        Endpoint endpoint) {

        if (endpoint == null || dispatcherContext == null) {
            return;
        }

        SOAPHeader header = synCtx.getEnvelope().getHeader();

        if (header != null) {
            OMElement csIDElm = header.getFirstChildWithName(CSID_QNAME);

            if (csIDElm != null) {
                String csID = csIDElm.getText();

                if (csID != null) {
                    if (log.isDebugEnabled()) {
                        log.debug("Using the client session id : '"
                                + csID + "' extracted from current message to update the session");
                    }
                    dispatcherContext.setEndpoint(csID, endpoint);
                } else if (log.isDebugEnabled()) {
                    log.debug("Couldn't find the client session id for the current message " +
                            "to update the session");
                }
            } else if (log.isDebugEnabled()) {
                log.debug("Couldn't find a SOAP header with the QName " + CSID_QNAME +
                        " for the current message to update the session");
            }
        }
    }


    public void unbind(MessageContext synCtx, DispatcherContext dispatcherContext) {

        if (dispatcherContext == null) {
            return;
        }

        SOAPHeader header = synCtx.getEnvelope().getHeader();

        if (header != null) {
            OMElement csIDElm = header.getFirstChildWithName(CSID_QNAME);

            if (csIDElm != null) {
                String csID = csIDElm.getText();

                if (csID != null) {
                    dispatcherContext.removeSession(csID);
                } else if (log.isDebugEnabled()) {
                    log.debug("Couldn't find the client session id for the current message " +
                            "to unbind the session");
                }
            } else if (log.isDebugEnabled()) {
                log.debug("Couldn't find a SOAP header with the QName " + CSID_QNAME +
                        " for the current message to unbind the session");
            }
        }
    }

    public boolean isServerInitiatedSession() {
        return false;
    }
}
