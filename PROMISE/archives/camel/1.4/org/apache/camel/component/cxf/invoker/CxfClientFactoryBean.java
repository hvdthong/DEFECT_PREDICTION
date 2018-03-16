package org.apache.camel.component.cxf.invoker;

import javax.xml.ws.soap.SOAPBinding;

import org.apache.cxf.BusException;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.ClientImpl;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.endpoint.EndpointException;
import org.apache.cxf.frontend.ClientFactoryBean;
import org.apache.cxf.jaxws.binding.soap.JaxWsSoapBindingConfiguration;
import org.apache.cxf.jaxws.support.JaxWsServiceFactoryBean;
import org.apache.cxf.service.factory.ReflectionServiceFactoryBean;
import org.apache.cxf.service.factory.ServiceConstructionException;



public class CxfClientFactoryBean extends ClientFactoryBean {
    private boolean isJSR181Enabled;
    
    public CxfClientFactoryBean() {
        super();        
    }
    
    public void setJSR181Enabled(boolean enabled) {
        if (enabled) {
            setServiceFactory(new JaxWsServiceFactoryBean());            
        } else {
            setServiceFactory(new ReflectionServiceFactoryBean());
        }
        isJSR181Enabled = enabled;
    }
    
    @Override
    public void setBindingId(String bind) {
        super.setBindingId(bind);
        if (isJSR181Enabled) {
            if (SOAPBinding.SOAP11HTTP_BINDING.equals(bind)
                || SOAPBinding.SOAP12HTTP_BINDING.equals(bind)) {
                setBindingConfig(new JaxWsSoapBindingConfiguration((JaxWsServiceFactoryBean)getServiceFactory()));
            } else if (SOAPBinding.SOAP11HTTP_MTOM_BINDING.equals(bind)
                || SOAPBinding.SOAP12HTTP_MTOM_BINDING.equals(bind)) {
                setBindingConfig(new JaxWsSoapBindingConfiguration((JaxWsServiceFactoryBean)getServiceFactory()));
                ((JaxWsSoapBindingConfiguration)getBindingConfig()).setMtomEnabled(true);
            }
        }    
    }
            
    protected void createClient(Endpoint ep) {
        CxfClient client = new CxfClient(getBus(), ep);
        setClient(client);
    }
    
    

}
