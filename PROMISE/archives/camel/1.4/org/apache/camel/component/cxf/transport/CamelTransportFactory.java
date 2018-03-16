package org.apache.camel.component.cxf.transport;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.camel.CamelContext;
import org.apache.cxf.Bus;
import org.apache.cxf.service.model.EndpointInfo;
import org.apache.cxf.transport.AbstractTransportFactory;
import org.apache.cxf.transport.Conduit;
import org.apache.cxf.transport.ConduitInitiator;
import org.apache.cxf.transport.ConduitInitiatorManager;
import org.apache.cxf.transport.Destination;
import org.apache.cxf.transport.DestinationFactory;
import org.apache.cxf.transport.DestinationFactoryManager;
import org.apache.cxf.ws.addressing.EndpointReferenceType;

/**
 * @version $Revision: 675833 $
 */
public class CamelTransportFactory extends AbstractTransportFactory implements ConduitInitiator, DestinationFactory {


    private static final Set<String> URI_PREFIXES = new HashSet<String>();

    private Collection<String> activationNamespaces;

    static {
    }

    private Bus bus;
    private CamelContext camelContext;

    @Resource(name = "bus")
    public void setBus(Bus b) {
        bus = b;
    }

    public Bus getBus() {
        return bus;
    }

    @Resource
    public void setActivationNamespaces(Collection<String> ans) {
        activationNamespaces = ans;
    }

    public CamelContext getCamelContext() {
        return camelContext;
    }

    @Resource(name = "camelContext")
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
        return new CamelDestination(camelContext, bus, this, endpointInfo);
    }

    public Set<String> getUriPrefixes() {
        return URI_PREFIXES;
    }

    @PostConstruct
    void registerWithBindingManager() {
        if (null == bus) {
            return;
        }
        ConduitInitiatorManager cim = bus.getExtension(ConduitInitiatorManager.class);
        if (null != cim && null != activationNamespaces) {
            for (String ns : activationNamespaces) {
                cim.registerConduitInitiator(ns, this);
            }
        }
        DestinationFactoryManager dfm = bus.getExtension(DestinationFactoryManager.class);
        if (null != dfm && null != activationNamespaces) {
            for (String ns : activationNamespaces) {
                dfm.registerDestinationFactory(ns, this);
            }
        }
    }
}

