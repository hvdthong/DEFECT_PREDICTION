package org.apache.camel.component.cxf.util;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.apache.cxf.binding.AbstractBindingFactory;
import org.apache.cxf.service.model.EndpointInfo;
import org.apache.cxf.transport.Destination;
import org.apache.cxf.transport.DestinationFactory;

public class NullDestinationFactory implements DestinationFactory {

    public Destination getDestination(EndpointInfo ei) throws IOException {
        ei.getBinding().setProperty(AbstractBindingFactory.DATABINDING_DISABLED, Boolean.TRUE);
        return new NullDestination();
    }

    public List<String> getTransportIds() {
        return null;
    }

    public Set<String> getUriPrefixes() {
        return null;
    }

}
