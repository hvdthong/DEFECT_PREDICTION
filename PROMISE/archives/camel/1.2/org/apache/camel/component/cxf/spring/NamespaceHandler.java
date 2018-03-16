package org.apache.camel.component.cxf.spring;

import org.apache.cxf.configuration.spring.StringBeanDefinitionParser;
import org.apache.cxf.frontend.spring.ServerFactoryBeanDefinitionParser;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class NamespaceHandler extends NamespaceHandlerSupport {
    public void init() {
        registerBeanDefinitionParser("cxfEndpoint", new CxfEndpointBeanDefinitionParser());      
        
    }
}
