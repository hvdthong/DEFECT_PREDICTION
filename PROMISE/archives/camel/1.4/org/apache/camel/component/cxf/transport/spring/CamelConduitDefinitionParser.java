package org.apache.camel.component.cxf.transport.spring;

import org.apache.camel.component.cxf.transport.CamelConduit;

public class CamelConduitDefinitionParser extends AbstractCamelContextBeanDefinitionParser {

    public CamelConduitDefinitionParser() {
        super();
        setBeanClass(CamelConduit.class);
    }

}
