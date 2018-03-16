package org.apache.camel.component.spring.integration.adapter.config;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import org.apache.camel.component.spring.integration.adapter.CamelTargetAdapter;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;

/**
 * Parser for the &lt;camelTarget/&gt; element
 * @author Willem Jiang
 *
 * @version $Revision: 711528 $
 */

public class CamelTargetAdapterParser extends AbstractCamelContextBeanDefinitionParaser {
    @Override
    protected Class<?> getBeanClass(Element element) {
        return CamelTargetAdapter.class;
    }
}
