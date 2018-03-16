package org.apache.camel.component.cxf;

import javax.xml.ws.WebServiceProvider;

import org.apache.camel.Processor;
import org.apache.camel.component.cxf.feature.MessageDataFormatFeature;
import org.apache.camel.component.cxf.feature.PayLoadDataFormatFeature;
import org.apache.camel.component.cxf.spring.CxfEndpointBean;
import org.apache.camel.component.cxf.util.CxfEndpointUtils;
import org.apache.camel.impl.DefaultConsumer;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.bus.spring.SpringBusFactory;
import org.apache.cxf.common.classloader.ClassLoaderUtils;
import org.apache.cxf.configuration.Configurer;
import org.apache.cxf.configuration.spring.ConfigurerImpl;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.frontend.ServerFactoryBean;

/**
 * A consumer of exchanges for a service in CXF
 *
 * @version $Revision: 735926 $
 */
public class CxfConsumer extends DefaultConsumer<CxfExchange> {
    private CxfEndpoint endpoint;
    private Server server;

    public CxfConsumer(CxfEndpoint endpoint, Processor processor) throws Exception {

        super(endpoint, processor);
        Bus bus = null;
        this.endpoint = endpoint;
        boolean isWebServiceProvider = false;
        if (endpoint.getApplicationContext() != null) {            
            if (endpoint.getCxfEndpointBean() != null) {
                bus = endpoint.getCxfEndpointBean().getBus();
            } else {
                SpringBusFactory busFactory = new SpringBusFactory(endpoint.getApplicationContext());
                bus = busFactory.createBus();
            }
            if (CxfEndpointUtils.getSetDefaultBus(endpoint)) {
                BusFactory.setDefaultBus(bus);
            }
        } else {
            bus = BusFactory.getDefaultBus();
        }
        
        Class serviceClass = CxfEndpointUtils.getServiceClass(endpoint);
        ServerFactoryBean svrBean = CxfEndpointUtils.getServerFactoryBean(serviceClass);
        isWebServiceProvider = CxfEndpointUtils.hasAnnotation(serviceClass,
                                                              WebServiceProvider.class);
        
        if (endpoint.isSpringContextEndpoint()) {
            endpoint.configure(svrBean);
            svrBean.setAddress(endpoint.getAddress());                       
            if (endpoint.getWsdlURL() != null) {
                svrBean.setWsdlURL(endpoint.getWsdlURL());
            }
        }
        
        svrBean.setServiceClass(serviceClass);
        
        if (CxfEndpointUtils.getServiceName(endpoint) != null) {
            svrBean.setServiceName(CxfEndpointUtils.getServiceName(endpoint));
        }
        if (CxfEndpointUtils.getServiceName(endpoint) != null) {
            svrBean.setEndpointName(CxfEndpointUtils.getPortName(endpoint));
        }
        
        DataFormat dataFormat = CxfEndpointUtils.getDataFormat(endpoint);

        svrBean.setInvoker(new CamelInvoker(this));

        if (!dataFormat.equals(DataFormat.POJO) && !isWebServiceProvider) {

            if (dataFormat.equals(DataFormat.PAYLOAD)) {
                svrBean.getFeatures().add(new PayLoadDataFormatFeature());
            } else if (dataFormat.equals(DataFormat.MESSAGE)) {
                svrBean.getFeatures().add(new MessageDataFormatFeature());
            }
        }
        svrBean.setBus(bus);
        svrBean.setStart(false);
        server = svrBean.create();

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
    
    public Server getServer() {
        return server;
    }

}
