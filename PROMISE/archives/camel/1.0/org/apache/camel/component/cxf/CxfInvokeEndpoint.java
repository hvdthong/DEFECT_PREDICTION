package org.apache.camel.component.cxf;

import java.util.Properties;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.cxf.Bus;
import org.apache.cxf.message.Message;

/**
 * The endpoint in the service engine
 *
 * @version $Revision: 537816 $
 */
public class CxfInvokeEndpoint extends DefaultEndpoint<CxfExchange> {
    private CxfBinding binding;
    private final CxfInvokeComponent component;
    private boolean inOut = true;
    private Properties properties;

    public CxfInvokeEndpoint(String uri, CxfInvokeComponent component, Properties properties) {
        super(uri, component);
        this.component = component;
        this.properties = properties;
    }

    public Producer<CxfExchange> createProducer() throws Exception {
        return new CxfInvokeProducer(this);
    }

    public Consumer<CxfExchange> createConsumer(Processor processor) throws Exception {
        return new CxfInvokeConsumer(this, processor);
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

    public CxfInvokeComponent getComponent() {
        return component;
    }
    
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public Bus getBus() {
        return component.getBus();
    }
    
	public boolean isSingleton() {
		return true;
	}

}
