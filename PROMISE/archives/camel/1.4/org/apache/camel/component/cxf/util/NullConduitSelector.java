package org.apache.camel.component.cxf.util;

import org.apache.cxf.endpoint.ConduitSelector;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.Conduit;

public class NullConduitSelector implements ConduitSelector {
    private Endpoint endpoint;
    private NullConduit nullConduit;

    public NullConduitSelector() {
        nullConduit = new NullConduit();
    }
    public void complete(Exchange exchange) {

    }

    public Endpoint getEndpoint() {
        return endpoint;
    }

    public void prepare(Message message) {

    }

    public Conduit selectConduit(Message message) {
        return nullConduit;
    }

    public void setEndpoint(Endpoint endpoint) {
        this.endpoint = endpoint;

    }

}
