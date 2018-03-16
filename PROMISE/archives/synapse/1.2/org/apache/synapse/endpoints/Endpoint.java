package org.apache.synapse.endpoints;

import org.apache.synapse.MessageContext;

/**
 * Endpoint defines the behavior common to all Synapse endpoints. Synapse endpoints should be able
 * to send the given Synapse message context, rather than just providing the information for sending
 * the message. The task a particular endpoint does in its send(...) method is specific to the endpoint.
 * For example a loadbalance endpoint may choose another endpoint using its load balance policy and
 * call its send(...) method while an address endpoint (leaf level) may send the message to an actual
 * endpoint url. Endpoints may contain zero or more endpoints in them and build up a hierarchical
 * structure of endpoints.
 */
public interface Endpoint {

    /**
     * Sends the message context according to an endpoint specific behavior.
     *
     * @param synMessageContext MessageContext to be sent.
     */
    public void send(MessageContext synMessageContext);

    /**
     * Endpoints that contain other endpoints should implement this method. It will be called if a
     * child endpoint causes an exception. Action to be taken on such failure is up to the implementation.
     * But it is good practice to first try addressing the issue. If it can't be addressed propagate the
     * exception to parent endpoint by calling parent endpoint's onChildEndpointFail(...) method.
     *
     * @param endpoint          The child endpoint which caused the exception.
     * @param synMessageContext MessageContext that was used in the failed attempt.
     */
    public void onChildEndpointFail(Endpoint endpoint, MessageContext synMessageContext);

    /**
     * Sets the parent endpoint for the current endpoint.
     *
     * @param parentEndpoint parent endpoint containing this endpoint. It should handle the onChildEndpointFail(...)
     *                       callback.
     */
    public void setParentEndpoint(Endpoint parentEndpoint);

    /**
     * Returns the name of the endpoint.
     *
     * @return Endpoint name.
     */
    public String getName();

    /**
     * Sets the name of the endpoint. Local registry use this name as the key for storing the
     * endpoint.
     *
     * @param name Name for the endpoint.
     */
    public void setName(String name);

    /**
     * Returns if the endpoint is currently active or not. Messages should not be sent to inactive
     * endpoints.
     *
     * @param synMessageContext MessageContext for the current message. This is required for
     *                          IndirectEndpoints where the actual endpoint is retrieved from the MessageContext. Other
     *                          Endpoint implementations may ignore this parameter.
     * @return true if the endpoint is in active state. false otherwise.
     */
    public boolean isActive(MessageContext synMessageContext);

    /**
     * Sets the endpoint as active or inactive. If an endpoint is detected as failed, it should be
     * set as inactive. But endpoints may be eventually set as active by the endpoint refresher to
     * avoid ignoring endpoints forever.
     *
     * @param active            true if active. false otherwise.
     * @param synMessageContext MessageContext for the current message. This is required for
     *                          IndirectEndpoints where the actual endpoint is retrieved from the MessageContext. Other
     *                          Endpoint implementations may ignore this parameter.
     */
    public void setActive(boolean active, MessageContext synMessageContext);
}
