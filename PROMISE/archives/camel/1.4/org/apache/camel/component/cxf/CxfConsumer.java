package org.apache.camel.component.cxf;

import java.util.ArrayList;
import java.util.List;

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
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.feature.AbstractFeature;
import org.apache.cxf.frontend.ServerFactoryBean;

/**
 * A consumer of exchanges for a service in CXF
 *
 * @version $Revision: 670454 $
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
            SpringBusFactory bf = new SpringBusFactory(endpoint.getApplicationContext());
            bus = bf.createBus();
            if (CxfEndpointUtils.getSetDefaultBus(endpoint)) {
                BusFactory.setDefaultBus(bus);
            }
        } else {
            bus = BusFactory.getDefaultBus();
        }
        ServerFactoryBean svrBean = null;

        if (endpoint.isSpringContextEndpoint()) {
            CxfEndpointBean endpointBean = endpoint.getCxfEndpointBean();
            svrBean = CxfEndpointUtils.getServerFactoryBean(endpointBean.getServiceClass());
            isWebServiceProvider = CxfEndpointUtils.hasAnnotation(endpointBean.getServiceClass(),
                                                                  WebServiceProvider.class);
            endpoint.configure(svrBean);

            Class serviceClass = ClassLoaderUtils.loadClass(endpoint.getServiceClass(), this.getClass());
            svrBean = CxfEndpointUtils.getServerFactoryBean(serviceClass);
            isWebServiceProvider = CxfEndpointUtils.hasAnnotation(serviceClass, WebServiceProvider.class);
            svrBean.setAddress(endpoint.getAddress());
            svrBean.setServiceClass(serviceClass);
            if (endpoint.getServiceName() != null) {
                svrBean.setServiceName(CxfEndpointUtils.getServiceName(endpoint));
            }
            if (endpoint.getPortName() != null) {
                svrBean.setEndpointName(CxfEndpointUtils.getPortName(endpoint));
            }
            if (endpoint.getWsdlURL() != null) {
                svrBean.setWsdlURL(endpoint.getWsdlURL());
            }
        }
        DataFormat dataFormat = CxfEndpointUtils.getDataFormat(endpoint);

        svrBean.setInvoker(new CamelInvoker(this));

        if (!dataFormat.equals(DataFormat.POJO) && !isWebServiceProvider) {
            List<AbstractFeature> features = new ArrayList<AbstractFeature>();

            if (dataFormat.equals(DataFormat.PAYLOAD)) {
                features.add(new PayLoadDataFormatFeature());
            } else if (dataFormat.equals(DataFormat.MESSAGE)) {
                features.add(new MessageDataFormatFeature());
            }
            svrBean.setFeatures(features);

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

}
