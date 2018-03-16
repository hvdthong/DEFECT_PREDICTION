package org.apache.camel.component.cxf.transport.spring;

import org.apache.camel.component.cxf.transport.CamelDestination;

public class CamelDestinationDefinitionParser extends AbstractCamelContextBeanDefinitionParser {
    public CamelDestinationDefinitionParser() {
        super();
        setBeanClass(CamelDestination.class);
    }
}
