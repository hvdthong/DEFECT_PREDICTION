package org.apache.camel.component.cxf.util;

import java.io.IOException;

import org.apache.cxf.message.Message;
import org.apache.cxf.transport.Conduit;
import org.apache.cxf.transport.Destination;
import org.apache.cxf.transport.MessageObserver;
import org.apache.cxf.ws.addressing.EndpointReferenceType;

public class NullDestination implements Destination {
    MessageObserver messageObserver;

    public NullDestination() {
    }

    public EndpointReferenceType getAddress() {
        return null;
    }

    public Conduit getBackChannel(Message inMessage, Message partialResponse, EndpointReferenceType address) throws IOException {
        return null;
    }

    public MessageObserver getMessageObserver() {
        return messageObserver;
    }

    public void shutdown() {
        messageObserver = null;
    }

    public void setMessageObserver(MessageObserver observer) {
        messageObserver = observer;
    }

}
