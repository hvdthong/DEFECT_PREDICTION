package org.apache.camel.component.cxf.spring;


import org.apache.cxf.frontend.AbstractWSDLBasedEndpointFactory;
import org.apache.cxf.service.factory.ReflectionServiceFactoryBean;

/**
 *
 */
public class CxfEndpointBean extends AbstractWSDLBasedEndpointFactory {
    public CxfEndpointBean() {
        setServiceFactory(new ReflectionServiceFactoryBean());
    }
}
