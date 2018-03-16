package org.apache.camel.component.cxf;

import java.net.URI;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;
import org.apache.cxf.Bus;
import org.apache.cxf.BusException;
import org.apache.cxf.bus.CXFBusFactory;
import org.apache.cxf.service.model.EndpointInfo;
import org.apache.cxf.transport.DestinationFactoryManager;
import org.apache.cxf.transport.local.LocalTransportFactory;
import org.xmlsoap.schemas.wsdl.http.AddressType;

/**

 * @version $Revision: 550734 $
 */
public class CxfComponent extends DefaultComponent<CxfExchange> {
    private LocalTransportFactory localTransportFactory;

    public CxfComponent() {
    }

    public CxfComponent(CamelContext context) {
        super(context);
    }

    @Override
    protected Endpoint<CxfExchange> createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        URI u = new URI(remaining);

        AddressType a = new AddressType();
        a.setLocation(remaining);
        endpointInfo.addExtensor(a);

        return new CxfEndpoint(uri, this, endpointInfo);
    }

    public LocalTransportFactory getLocalTransportFactory() throws BusException {
        if (localTransportFactory == null) {
            localTransportFactory = findLocalTransportFactory();
            if (localTransportFactory == null) {
                localTransportFactory = new LocalTransportFactory();
            }
        }
        return localTransportFactory;
    }

    public void setLocalTransportFactory(LocalTransportFactory localTransportFactory) {
        this.localTransportFactory = localTransportFactory;
    }

    protected LocalTransportFactory findLocalTransportFactory() throws BusException {
        Bus bus = CXFBusFactory.getDefaultBus();
        DestinationFactoryManager dfm = bus.getExtension(DestinationFactoryManager.class);
        return (LocalTransportFactory) dfm.getDestinationFactory(LocalTransportFactory.TRANSPORT_ID);
    }
}
