package org.apache.camel.component.cxf;

import org.apache.camel.Consumer;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.component.cxf.spring.CxfEndpointBean;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spring.SpringCamelContext;
import org.apache.cxf.configuration.spring.ConfigurerImpl;
import org.apache.cxf.message.Message;
import org.springframework.context.ApplicationContext;


/**
 *
 * @version $Revision: 670454 $
 */
public class CxfEndpoint extends DefaultEndpoint<CxfExchange> {
    private final CxfComponent component;
    private final String address;
    private String wsdlURL;
    private String serviceClass;
    private String portName;
    private String serviceName;
    private String dataFormat;
    private String beanId;
    private boolean isWrapped;
    private boolean isSpringContextEndpoint;
    private boolean inOut = true;
    private Boolean isSetDefaultBus;
    private ConfigurerImpl configurer;
    private CxfEndpointBean cxfEndpointBean;



    public CxfEndpoint(String uri, String address, CxfComponent component) {
        super(uri, component);
        this.component = component;
        this.address = address;
        if (address.startsWith(CxfConstants.SPRING_CONTEXT_ENDPOINT)) {
            isSpringContextEndpoint = true;
            beanId = address.substring(CxfConstants.SPRING_CONTEXT_ENDPOINT.length());
                beanId = beanId.substring(2);
            }
            SpringCamelContext context = (SpringCamelContext) this.getCamelContext();
            configurer = new ConfigurerImpl(context.getApplicationContext());
            cxfEndpointBean = (CxfEndpointBean) context.getApplicationContext().getBean(beanId);
            assert cxfEndpointBean != null;
        }
    }

    public Producer<CxfExchange> createProducer() throws Exception {
        return new CxfProducer(this);
    }

    public Consumer<CxfExchange> createConsumer(Processor processor) throws Exception {
        return new CxfConsumer(this, processor);
    }

    public CxfExchange createExchange() {
        return new CxfExchange(getCamelContext(), getExchangePattern());
    }

    public CxfExchange createExchange(ExchangePattern pattern) {
        return new CxfExchange(getCamelContext(), pattern);
    }

    public CxfExchange createExchange(Message inMessage) {
        return new CxfExchange(getCamelContext(), getExchangePattern(), inMessage);
    }

    public String getDataFormat() {
        return dataFormat;
    }

    public void setDataFormat(String format) {
        dataFormat = format;
    }

    public boolean isSpringContextEndpoint() {
        return isSpringContextEndpoint;
    }

    public String getAddress() {
        return address;
    }

    public String getWsdlURL() {
        return wsdlURL;
    }

    public void setWsdlURL(String url) {
        wsdlURL = url;
    }

    public void setSetDefaultBus(Boolean set) {
        isSetDefaultBus = set;
    }

    public Boolean isSetDefaultBus() {
        return isSetDefaultBus;
    }

    public String getServiceClass() {
        return serviceClass;

    }

    public void setServiceClass(String className) {
        serviceClass = className;
    }

    public void setPortName(String port) {
        portName = port;
    }

    public void setServiceName(String service) {
        serviceName = service;
    }

    public String getPortName() {
        return portName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public boolean isInOut() {
        return inOut;
    }

    public void setInOut(boolean inOut) {
        this.inOut = inOut;
    }

    public boolean isWrapped() {
        return isWrapped;
    }

    public void setWrapped(boolean wrapped) {
        isWrapped = wrapped;
    }


    public CxfComponent getComponent() {
        return component;
    }

    public boolean isSingleton() {
        return true;
    }

    public String getBeanId() {
        return beanId;
    }

    public CxfEndpointBean getCxfEndpointBean() {
        return cxfEndpointBean;
    }

    public void configure(Object beanInstance) {
        configurer.configureBean(beanId, beanInstance);
    }

    public ApplicationContext getApplicationContext() {
        if (getCamelContext() instanceof SpringCamelContext) {
            SpringCamelContext context = (SpringCamelContext) getCamelContext();
            return context.getApplicationContext();
        } else {
            return null;
        }
    }


}
