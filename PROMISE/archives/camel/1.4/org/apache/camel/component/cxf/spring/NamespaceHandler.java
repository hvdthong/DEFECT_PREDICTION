package org.apache.camel.component.cxf.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class NamespaceHandler extends NamespaceHandlerSupport {
    public void init() {
        registerBeanDefinitionParser("cxfEndpoint", new CxfEndpointBeanDefinitionParser());

    }
}
