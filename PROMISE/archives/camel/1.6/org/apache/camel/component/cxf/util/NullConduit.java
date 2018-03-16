package org.apache.camel.component.cxf.util;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.Conduit;
import org.apache.cxf.transport.Destination;
import org.apache.cxf.transport.MessageObserver;
import org.apache.cxf.ws.addressing.EndpointReferenceType;

public class NullConduit implements Conduit {

    public void close() {
    }

    public void close(Message message) throws IOException {
        OutputStream outputStream = message.getContent(OutputStream.class);
        if (outputStream != null) {
            outputStream.close();
        }
    }

    public Destination getBackChannel() {
        return null;
    }

    public EndpointReferenceType getTarget() {
        return null;
    }

    public void prepare(Message message) throws IOException {
        CachedOutputStream outputStream = new CachedOutputStream();
        message.setContent(OutputStream.class, outputStream);
    }

    public void setMessageObserver(MessageObserver observer) {
    }

}
