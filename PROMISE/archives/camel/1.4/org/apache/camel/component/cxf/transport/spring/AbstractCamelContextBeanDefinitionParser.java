package org.apache.camel.component.cxf.transport.spring;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.apache.camel.util.ObjectHelper;
import org.apache.cxf.configuration.spring.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;


public class AbstractCamelContextBeanDefinitionParser extends AbstractBeanDefinitionParser {
    private static final String DEFAULT_CAMEL_CONTEXT_NAME = "camelContext";

    private String getContextId(String contextId) {
        if (ObjectHelper.isNullOrBlank(contextId)) {
            return DEFAULT_CAMEL_CONTEXT_NAME;
        } else {
            return contextId;
        }
    }

    protected void wireCamelContext(BeanDefinitionBuilder bean, String camelContextId) {
        bean.addPropertyReference("camelContext", camelContextId);
    }

    protected void doParse(Element element, ParserContext ctx, BeanDefinitionBuilder bean) {
        bean.setAbstract(true);
        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node n = children.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                String name = n.getLocalName();
                if ("camelContext".equals(name)) {
                    BeanDefinition bd = ctx.getDelegate().parseCustomElement((Element)n);
                    String contextId = (String)bd.getPropertyValues().getPropertyValue("id").getValue();
                    wireCamelContext(bean, getContextId(contextId));
                } else if ("camelContextRef".equals(name)) {
                    String contextId = n.getTextContent();
                    wireCamelContext(bean, getContextId(contextId));
                }
            }
        }
    }
}
