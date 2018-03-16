package org.apache.camel.component.cxf.invoker;

import java.util.Map;

import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;

public interface InvokingContext {

    /**
     * This method is called when the router is preparing an outbound message
     * (orignated from the router's client) to be sent to the target CXF server.
     * It sets the content in the given (out) message object.
     */
    void setRequestOutMessageContent(Message message, Map<Class, Object> contents);

    /**
     * This method is call when the CxfClient receives a response from a CXF server and needs
     * to extract the response object from the message.
     */
    Object getResponseObject(Exchange exchange, Map<String, Object> responseContext);

    /**
     * This method is called when the routing interceptor has received a response message
     * from the target CXF server and needs to set the response in the outgoing message
     * that is to be sent to the client.
     */
    void setResponseContent(Message outMessage, Object resultPayload);

    /**
     * This method is called when the routing interceptor has intercepted a message from
     * the client and needs to extract the request content from the message.  It retreives
     * and receives the request content from the incoming message.
     */
    Map<Class, Object> getRequestContent(Message inMessage);

}
