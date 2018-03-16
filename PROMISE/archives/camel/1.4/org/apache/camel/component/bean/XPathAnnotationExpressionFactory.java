package org.apache.camel.component.bean;

import java.lang.annotation.Annotation;

import org.apache.camel.CamelContext;
import org.apache.camel.Expression;
import org.apache.camel.builder.xml.XPathBuilder;
import org.apache.camel.language.LanguageAnnotation;
import org.apache.camel.language.NamespacePrefix;
import org.apache.camel.language.XPath;

/**
 * Factory for the XPath expression annotations.
 *
 * @version $Revision: 660275 $
 */
public class XPathAnnotationExpressionFactory extends DefaultAnnotationExpressionFactory {

    @Override
    public Expression createExpression(CamelContext camelContext, Annotation annotation, LanguageAnnotation languageAnnotation, Class expressionReturnType) {
        String xpath = getExpressionFromAnnotation(annotation);
        XPathBuilder builder = XPathBuilder.xpath(xpath);
        if (annotation instanceof XPath) {
            XPath xpathAnnotation = (XPath) annotation;
            NamespacePrefix[] namespaces = xpathAnnotation.namespaces();
            if (namespaces != null) {
                for (NamespacePrefix namespacePrefix : namespaces) {
                    builder = builder.namespace(namespacePrefix.prefix(), namespacePrefix.uri());
                }
            }
        }
        return builder;
    }
}
