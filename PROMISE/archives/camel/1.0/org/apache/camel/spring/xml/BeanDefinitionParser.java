package org.apache.camel.spring.xml;

import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.w3c.dom.Element;

/**
 * A base class for a parser for a bean.
 *
 * @version $Revision: 1.1 $
 */
public class BeanDefinitionParser extends AbstractSimpleBeanDefinitionParser {
    private Class type;

    protected BeanDefinitionParser() {
    }

    public BeanDefinitionParser(Class type) {
        this.type = type;
    }

    protected Class getBeanClass(Element element) {
        if (type == null) {
            type = loadType();
        }
        return type;
    }

    protected Class loadType() {
        throw new IllegalArgumentException("No type specified!");
    }

    @Override
    protected boolean isEligibleAttribute(String attributeName) {
        return attributeName != null && super.isEligibleAttribute(attributeName) && !attributeName.equals("xmlns");
    }

    
}
