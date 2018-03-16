package org.apache.camel.spring.xml;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

/**
 * A parser of the various scripting language expressions
 * @version $Revision: 1.1 $
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
        builder.addConstructorArg(engine);
        super.doParse(element, parserContext, builder);
        String scriptText = DomUtils.getTextValue(element).trim();
        if (scriptText.length() > 0) {
            builder.addPropertyValue("scriptText", scriptText);
        }
    }

}
