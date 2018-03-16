package org.apache.camel.component.spring.integration.adapter.config;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import org.apache.camel.component.spring.integration.adapter.CamelSourceAdapter;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;

/**
 * Parser for the &lt;camelSource/&gt; element
 * @author Willem Jiang
 *
 * @version $Revision: 655008 $
 */
public class CamelSourceAdpaterParser extends AbstractCamelContextBeanDefinitionParaser {
    @Override
    protected Class<?> getBeanClass(Element element) {
        return CamelSourceAdapter.class;
    }

}
