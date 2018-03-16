package org.apache.camel.component.spring.integration.adapter.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * The name space handler Spring Integration Camel Adapter
 *
 * @author Willem Jiang
 *
 * @version $Revision: 655008 $
 */
public class NamespaceHandler extends NamespaceHandlerSupport {
    public void init() {
        registerBeanDefinitionParser("camelSource", new CamelSourceAdpaterParser());
        registerBeanDefinitionParser("camelTarget", new CamelTargetAdapterParser());
    }
}
