package org.apache.camel.component.cxf.spring;


import java.util.List;

import org.apache.cxf.frontend.AbstractWSDLBasedEndpointFactory;
import org.apache.cxf.service.factory.ReflectionServiceFactoryBean;

/**
 *
 */
public class CxfEndpointBean extends AbstractWSDLBasedEndpointFactory {
    private List handlers;

    public CxfEndpointBean() {
        this(new ReflectionServiceFactoryBean());
    }
    
    public CxfEndpointBean(ReflectionServiceFactoryBean factory) {
        setServiceFactory(factory);
    }
    
    public List getHandlers() {
        return handlers;
    }
    
    public void setHandlers(List handlers) {
        this.handlers = handlers;
    }
}
