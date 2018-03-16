package org.apache.camel.component.stream;

import org.apache.camel.impl.DefaultMessage;

/**
 * @deprecated Camel Stream uses a DefaultMessage to contain the body. Will be removed in Camel 2.0
 */
public class StreamMessage extends DefaultMessage {
    private Object o;

    public StreamMessage(Object o) {
        this.o = o;
    }

    @Override
    protected Object createBody() {
        return o;
    }

    @Override
    public Object getBody() {
        return o;
    }

    @Override
    public String toString() {
        return o.toString();
    }

}
