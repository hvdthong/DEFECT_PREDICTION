package org.apache.camel.builder.saxon;

import org.apache.camel.CamelContext;
import org.apache.camel.Expression;
import org.apache.camel.component.bean.DefaultAnnotationExpressionFactory;
import org.apache.camel.language.LanguageAnnotation;
import org.apache.camel.language.NamespacePrefix;

import java.lang.annotation.Annotation;

/**
 * @version $Revision: 1.1 $
 */
public class XQueryAnnotationExpressionFactory extends DefaultAnnotationExpressionFactory {
    @Override
    public Expression createExpression(CamelContext camelContext, Annotation annotation, LanguageAnnotation languageAnnotation, Class expressionReturnType) {
        String XQuery = getExpressionFromAnnotation(annotation);
        XQueryBuilder builder = XQueryBuilder.xquery(XQuery);
        if (annotation instanceof XQuery) {
            XQuery XQueryAnnotation = (XQuery) annotation;
            NamespacePrefix[] namespaces = XQueryAnnotation.namespaces();
            if (namespaces != null) {
                for (NamespacePrefix namespacePrefix : namespaces) {
                }
            }
        }
        return builder;
    }
}
