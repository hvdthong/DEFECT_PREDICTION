package org.apache.camel.component.cxf.transport;

import org.apache.camel.CamelContext;
import org.apache.cxf.Bus;
import org.apache.cxf.configuration.Configurer;
import org.apache.cxf.service.model.EndpointInfo;
import org.apache.cxf.transport.AbstractTransportFactory;
import org.apache.cxf.transport.Conduit;
import org.apache.cxf.transport.ConduitInitiator;
import org.apache.cxf.transport.Destination;
import org.apache.cxf.transport.DestinationFactory;
import org.apache.cxf.ws.addressing.EndpointReferenceType;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @version $Revision: 522906 $
 */
public class CamelTransportFactory extends AbstractTransportFactory implements ConduitInitiator, DestinationFactory {
    private static final Set<String> URI_PREFIXES = new HashSet<String>();

    static {
    }

    private Bus bus;
    private CamelContext camelContext;

    @Resource
    public void setBus(Bus b) {
        bus = b;
    }

    public Bus getBus() {
        return bus;
    }

    public CamelContext getCamelContext() {
        return camelContext;
    }

    @Resource
    public void setCamelContext(CamelContext camelContext) {
        this.camelContext = camelContext;
    }

    public Conduit getConduit(EndpointInfo targetInfo) throws IOException {
        return getConduit(targetInfo, null);
    }

    public Conduit getConduit(EndpointInfo endpointInfo, EndpointReferenceType target) throws IOException {
        return new CamelConduit(camelContext, bus, endpointInfo, target);
    }

    public Destination getDestination(EndpointInfo endpointInfo) throws IOException {
        CamelDestination destination = new CamelDestination(camelContext, bus, this, endpointInfo);
        Configurer configurer = bus.getExtension(Configurer.class);
        if (null != configurer) {
            configurer.configureBean(destination);
        }
        return destination;
    }

    public Set<String> getUriPrefixes() {
        return URI_PREFIXES;
    }
}

