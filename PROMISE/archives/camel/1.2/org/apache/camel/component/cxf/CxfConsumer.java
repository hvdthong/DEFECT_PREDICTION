package org.apache.camel.component.cxf;

import java.net.URI;

import org.apache.camel.Processor;
import org.apache.camel.component.cxf.spring.CxfEndpointBean;
import org.apache.camel.component.cxf.util.CxfEndpointUtils;
import org.apache.camel.component.cxf.util.UriUtils;
import org.apache.camel.impl.DefaultConsumer;
import org.apache.camel.spring.SpringCamelContext;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.common.classloader.ClassLoaderUtils;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.endpoint.ServerImpl;
import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.Destination;
import org.apache.cxf.transport.MessageObserver;


/**
 * A consumer of exchanges for a service in CXF
 * 
 * @version $Revision: 583092 $
 */
public class CxfConsumer extends DefaultConsumer<CxfExchange> {
    private CxfEndpoint endpoint;    
    private Server server;
    

    public CxfConsumer(CxfEndpoint endpoint, Processor processor) throws Exception {
       
        super(endpoint, processor);        
        this.endpoint = endpoint;        
        
        try {
            Bus bus = BusFactory.getDefaultBus();
            ServerFactoryBean svrBean = null;
            if (endpoint.isSpringContextEndpoint()) {
                CxfEndpointBean endpointBean = endpoint.getCxfEndpointBean();
                svrBean = CxfEndpointUtils.getServerFactoryBean(endpointBean.getServiceClass());
                endpoint.configure(svrBean);
                CxfEndpointBean cxfEndpointBean = endpoint.getCxfEndpointBean();
                if (cxfEndpointBean.getServiceName() != null) {
                    svrBean.getServiceFactory().setServiceName(cxfEndpointBean.getServiceName());
                } 
                if (cxfEndpointBean.getEndpointName() != null) {
                    svrBean.getServiceFactory().setEndpointName(cxfEndpointBean.getEndpointName());
                } 
                
                Class serviceClass = ClassLoaderUtils.loadClass(endpoint.getServiceClass(), this.getClass()); 
                svrBean = CxfEndpointUtils.getServerFactoryBean(serviceClass);                           
                svrBean.setAddress(endpoint.getAddress());
                svrBean.setServiceClass(serviceClass);
                if (endpoint.getServiceName() != null) {
                    svrBean.getServiceFactory().setServiceName(CxfEndpointUtils.getServiceName(endpoint));                
                }
                if (endpoint.getPortName() != null) {
                    svrBean.getServiceFactory().setEndpointName(CxfEndpointUtils.getPortName(endpoint));
                }    
                if (endpoint.getWsdlURL() != null) {                
                    svrBean.setWsdlURL(endpoint.getWsdlURL());
                }
            }
            DataFormat dataFormat = CxfEndpointUtils.getDataFormat(endpoint);
            if (dataFormat.equals(DataFormat.POJO)) {
                svrBean.setInvoker(new CamelInvoker(this));
            }
            svrBean.setBus(bus);
            svrBean.setStart(false);
            server = svrBean.create();            
            if (!dataFormat.equals(DataFormat.POJO)) {
                CxfMessageObserver observer = new CxfMessageObserver(this, server.getEndpoint(), bus , dataFormat);
                ServerImpl serverImpl = (ServerImpl)server;
                serverImpl.setMessageObserver(observer);
            } 
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();        
        
        server.start();
    }

    @Override
    protected void doStop() throws Exception {
        server.stop();
        super.doStop();
    }
    
    public CxfEndpoint getEndpoint() {
        return endpoint;
    }

    
}
