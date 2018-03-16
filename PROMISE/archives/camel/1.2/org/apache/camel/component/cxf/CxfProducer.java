package org.apache.camel.component.cxf;

import java.util.List;

import org.apache.camel.CamelException;
import org.apache.camel.Exchange;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.component.cxf.invoker.CxfClient;
import org.apache.camel.component.cxf.invoker.CxfClientFactoryBean;
import org.apache.camel.component.cxf.invoker.InvokingContext;
import org.apache.camel.component.cxf.spring.CxfEndpointBean;
import org.apache.camel.component.cxf.util.CxfEndpointUtils;
import org.apache.camel.impl.DefaultProducer;
import org.apache.camel.util.ObjectHelper;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.binding.Binding;
import org.apache.cxf.binding.BindingFactory;
import org.apache.cxf.binding.BindingFactoryManager;
import org.apache.cxf.common.classloader.ClassLoaderUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.frontend.ClientFactoryBean;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageImpl;
import org.apache.cxf.service.model.BindingOperationInfo;
import org.apache.cxf.transport.Conduit;

import java.io.InputStream;
import java.net.MalformedURLException;

/**
 * Sends messages from Camel into the CXF endpoint
 * 
 * @version $Revision: 583092 $
 */
public class CxfProducer extends DefaultProducer <CxfExchange> {
    private CxfEndpoint endpoint;
    private Client client;
    private DataFormat dataFormat;
    
    

    public CxfProducer(CxfEndpoint endpoint) throws CamelException {
        super(endpoint);
        this.endpoint = endpoint;
        dataFormat = CxfEndpointUtils.getDataFormat(endpoint);
        if (dataFormat.equals(DataFormat.POJO)) {
            client = createClientFormClientFactoryBean(null);
        } else {
            client = createClientForStreamMessge();           
        }
    }
    
    private Client createClientForStreamMessge() throws CamelException {
        CxfClientFactoryBean cfb = new CxfClientFactoryBean();
        if (null != endpoint.getServiceClass()) {
            try {
                Class serviceClass = ClassLoaderUtils.loadClass(endpoint.getServiceClass(), this.getClass());
                boolean jsr181Enabled = CxfEndpointUtils.hasWebServiceAnnotation(serviceClass);
                cfb.setJSR181Enabled(jsr181Enabled);
            } catch (ClassNotFoundException e) {
                throw new CamelException(e);
            }
        }
        return createClientFormClientFactoryBean(cfb);
    }
   
    private Client createClientFormClientFactoryBean(ClientFactoryBean cfb) throws CamelException {              
        Bus bus = BusFactory.getDefaultBus();
        if (endpoint.isSpringContextEndpoint()) {
            CxfEndpointBean endpointBean = endpoint.getCxfEndpointBean();
            if (cfb == null) {
                cfb = CxfEndpointUtils.getClientFactoryBean(endpointBean.getServiceClass());
            }    
            endpoint.configure(cfb);
            CxfEndpointBean cxfEndpointBean = endpoint.getCxfEndpointBean();
            if (cxfEndpointBean.getServiceName() != null) {
                cfb.getServiceFactory().setServiceName(cxfEndpointBean.getServiceName());
            } 
            if (cxfEndpointBean.getEndpointName() != null) {
                cfb.getServiceFactory().setEndpointName(cxfEndpointBean.getEndpointName());
            } 
            if (null != endpoint.getServiceClass()) {
                try {
                    Class serviceClass = ClassLoaderUtils.loadClass(endpoint.getServiceClass(), this.getClass());
                    if (cfb == null) {
                        cfb = CxfEndpointUtils.getClientFactoryBean(serviceClass);
                    } 
                    cfb.setAddress(endpoint.getAddress());
                    if (null != endpoint.getServiceClass()) {            
                        cfb.setServiceClass(ObjectHelper.loadClass(endpoint.getServiceClass()));
                    } 
                    if (null != endpoint.getWsdlURL()) {
                        cfb.setWsdlURL(endpoint.getWsdlURL());
                    }                
                } catch (ClassNotFoundException e) {
                    throw new CamelException(e);
                }
                if (cfb == null) {
                    cfb = new ClientFactoryBean();
                }    
                if (null != endpoint.getWsdlURL()) {
                    cfb.setWsdlURL(endpoint.getWsdlURL());
                } else {
                    throw new CamelException("Insufficiency of the endpoint info");
                }
            }
            if (endpoint.getServiceName() != null) {
                cfb.getServiceFactory().setServiceName(CxfEndpointUtils.getServiceName(endpoint));
            }
            if (endpoint.getPortName() != null) {
                cfb.getServiceFactory().setEndpointName(CxfEndpointUtils.getPortName(endpoint));
               
            }    
            if (endpoint.getWsdlURL() != null) {                
                cfb.setWsdlURL(endpoint.getWsdlURL());
            }
        }    
        cfb.setBus(bus);        
        return cfb.create();
    }
   
    public void process(Exchange exchange) {
        CxfExchange cxfExchange = endpoint.createExchange(exchange);
        process(cxfExchange);
    }

    public void process(CxfExchange exchange) {
        CxfBinding cxfBinding = endpoint.getBinding();
        Message inMessage = cxfBinding.createCxfMessage(exchange);
        try {
            if (dataFormat.equals(DataFormat.POJO)) {
                List paraments = inMessage.getContent(List.class);
                String operation = inMessage.getContent(String.class);
                Message response = new MessageImpl();            
                if (operation != null && paraments != null) {                
                    try {
                        Object[] result = client.invoke(operation, paraments.toArray());                
                        response.setContent(Object[].class, result);
                        cxfBinding.storeCxfResponse(exchange, response);
                    } catch (Exception ex) {
                        response.setContent(Exception.class, ex);
                        cxfBinding.storeCxfFault(exchange, response);                        
                    }
                }  
            } else {
                org.apache.cxf.message.Exchange ex = exchange.getExchange();
                InvokingContext invokingContext = ex.get(InvokingContext.class);
                Object params = invokingContext.getRequestContent(inMessage);
                CxfClient cxfClient = (CxfClient) client;
                Object result = cxfClient.dispatch(params, null, ex);
                BindingOperationInfo boi = ex.get(BindingOperationInfo.class);
                Message response = null;                
                if (boi == null) {
                    response = new MessageImpl(); 
                } else {
                    Endpoint ep = ex.get(Endpoint.class);                    
                    response = ep.getBinding().createMessage();
                }                
                response.setExchange(ex);
                ex.setOutMessage(response);                
                invokingContext.setResponseContent(response, result);
                cxfBinding.storeCxfResponse(exchange, response);
            }
        } catch (Exception e) {
            throw new RuntimeCamelException(e);
        }     
                
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart(); 
    }

    @Override
    protected void doStop() throws Exception {
        super.doStop();        
    }

}
