package org.apache.camel.component.cxf.transport.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class NamespaceHandler extends NamespaceHandlerSupport {
    public void init() {
        registerBeanDefinitionParser("conduit", new CamelConduitDefinitionParser());
        registerBeanDefinitionParser("destination", new CamelDestinationDefinitionParser());
    }

}
