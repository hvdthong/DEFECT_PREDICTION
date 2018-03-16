package org.apache.camel.spring.handler;

import org.w3c.dom.Element;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;

/**
 * A parser of the various scripting language expressions
 *
 * @version $Revision: 663832 $
 */
public class ScriptDefinitionParser extends LazyLoadingBeanDefinitionParser {
    private final String scriptEngineName;

    public ScriptDefinitionParser(String scriptEngineName) {
        super("org.apache.camel.builder.script.ScriptBuilder", "camel-script");
        this.scriptEngineName = scriptEngineName;
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        String engine = scriptEngineName;
        if (engine == null) {
            engine = element.getAttribute("language");
        }
        builder.addConstructorArgValue(engine);
        super.doParse(element, parserContext, builder);
        String scriptText = DomUtils.getTextValue(element).trim();
        if (scriptText.length() > 0) {
            builder.addPropertyValue("scriptText", scriptText);
        }
    }
}
