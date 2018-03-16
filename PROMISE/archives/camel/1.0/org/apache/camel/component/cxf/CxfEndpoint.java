package org.apache.camel.component.cxf;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.cxf.BusException;
import org.apache.cxf.message.Message;
import org.apache.cxf.service.model.EndpointInfo;
import org.apache.cxf.transport.local.LocalTransportFactory;

/**
 *
 * @version $Revision: 550734 $
 */
public class CxfEndpoint extends DefaultEndpoint<CxfExchange> {
    private CxfBinding binding;
    private final CxfComponent component;
    private final EndpointInfo endpointInfo;
    private boolean inOut = true;

    public CxfEndpoint(String uri, CxfComponent component, EndpointInfo endpointInfo) {
        super(uri, component);
        this.component = component;
        this.endpointInfo = endpointInfo;
    }

    public Producer<CxfExchange> createProducer() throws Exception {
        return new CxfProducer(this, getLocalTransportFactory());
    }

    public Consumer<CxfExchange> createConsumer(Processor processor) throws Exception {
        return new CxfConsumer(this, processor, getLocalTransportFactory());
    }

    public CxfExchange createExchange() {
        return new CxfExchange(getContext(), getBinding());
    }

    public CxfExchange createExchange(Message inMessage) {
        return new CxfExchange(getContext(), getBinding(), inMessage);
    }

    public CxfBinding getBinding() {
        if (binding == null) {
            binding = new CxfBinding();
        }
        return binding;
    }

    public void setBinding(CxfBinding binding) {
        this.binding = binding;
    }

    public boolean isInOut() {
        return inOut;
    }

    public void setInOut(boolean inOut) {
        this.inOut = inOut;
    }

    public LocalTransportFactory getLocalTransportFactory() throws BusException {
        return component.getLocalTransportFactory();
    }

    public EndpointInfo getEndpointInfo() {
        return endpointInfo;
    }

    public CxfComponent getComponent() {
        return component;
    }
    
	public boolean isSingleton() {
		return true;
	}

}
