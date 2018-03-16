package org.apache.synapse.endpoints.dispatch;

import org.apache.synapse.endpoints.Endpoint;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

/**
 * Dispatches sessions based on HTTP cookies. Session is initiated by the server in the first response
 * when it sends "Set-Cookie" HTTP header with the session ID. For all successive messages client
 * should send "Cookie" HTTP header with session ID send by the server.
 */
public class HttpSessionDispatcher implements Dispatcher {

    /**
     * Map to store session -> endpoint mappings. Synchronized map is used as this is accessed by
     * multiple threds (e.g. multiple clients different sessions).
     */
    Map sessionMap = Collections.synchronizedMap(new HashMap());

    /**
     * Check if "Cookie" HTTP header is available. If so, check if that cookie is in the session map.
     * If cookie is available, there is a session for this cookie. return the (server) endpoint for
     * that session.
     *
     * @param synCtx MessageContext possibly containing a "Cookie" HTTP header.
     *
     * @return Endpoint Server endpoint for the given HTTP session.
     */
    public Endpoint getEndpoint(MessageContext synCtx) {

        Endpoint endpoint = null;

        org.apache.axis2.context.MessageContext axis2MessageContext =
                ((Axis2MessageContext) synCtx).getAxis2MessageContext();

        Object o = axis2MessageContext.getProperty("TRANSPORT_HEADERS");
        if (o != null && o instanceof Map) {
            Map headerMap = (Map) o;
            Object cookie = headerMap.get("Cookie");

            if (cookie != null) {
                Object e = sessionMap.get(cookie);
                if (e != null) {
                    endpoint = (Endpoint) e;
                }
            }
        }

        return endpoint;
    }

    /**
     * Searches for "Set-Cookie" HTTP header in the message context. If found and that given
     * session ID is not already in the session map update the session map by mapping the cookie
     * to the endpoint.
     *
     * @param synCtx MessageContext possibly containing the "Set-Cookie" HTTP header.
     * @param endpoint Endpoint to be mapped to the session.
     */
    public void updateSession(MessageContext synCtx, Endpoint endpoint) {

        org.apache.axis2.context.MessageContext axis2MessageContext =
                ((Axis2MessageContext) synCtx).getAxis2MessageContext();

        Object o = axis2MessageContext.getProperty("TRANSPORT_HEADERS");
        if (o != null && o instanceof Map) {
            Map headerMap = (Map) o;
            Object cookie = headerMap.get("Set-Cookie");

            if (cookie != null) {
                synchronized(sessionMap) {
                    if (!sessionMap.containsKey(cookie)) {
                        sessionMap.put(cookie, endpoint);
                    }
                }
            }
        }
    }

    public void unbind(MessageContext synCtx) {

        org.apache.axis2.context.MessageContext axis2MessageContext =
                ((Axis2MessageContext) synCtx).getAxis2MessageContext();

        Object o = axis2MessageContext.getProperty("TRANSPORT_HEADERS");
        if (o != null && o instanceof Map) {
            Map headerMap = (Map) o;
            Object cookie = headerMap.get("Cookie");

            if (cookie != null) {
                sessionMap.remove(cookie);
            }
        }
    }

    /**
     * HTTP sessions are initiated by the server.
     *
     * @return true
     */
    public boolean isServerInitiatedSession() {
        return true;
    }
}
