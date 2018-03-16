package org.apache.camel.component.xquery;

import java.lang.annotation.Annotation;

import net.sf.saxon.functions.Collection;
import org.apache.camel.CamelContext;
import org.apache.camel.Expression;
import org.apache.camel.component.bean.DefaultAnnotationExpressionFactory;
import org.apache.camel.language.LanguageAnnotation;
import org.apache.camel.language.NamespacePrefix;

/**
 * @version $Revision: 641680 $
 */
public class XQueryAnnotationExpressionFactory extends DefaultAnnotationExpressionFactory {
    @Override
    public Expression createExpression(CamelContext camelContext, Annotation annotation,
                                       LanguageAnnotation languageAnnotation, Class expressionReturnType) {
        String xQuery = getExpressionFromAnnotation(annotation);
        XQueryBuilder builder = XQueryBuilder.xquery(xQuery);
        if (annotation instanceof XQuery) {
            XQuery xQueryAnnotation = (XQuery)annotation;
            NamespacePrefix[] namespaces = xQueryAnnotation.namespaces();
            if (namespaces != null) {
                for (NamespacePrefix namespacePrefix : namespaces) {
                }
            }
        }
        if (expressionReturnType.isAssignableFrom(String.class)) {
            builder.setResultsFormat(ResultFormat.String);
        } else if (expressionReturnType.isAssignableFrom(Collection.class)) {
            builder.setResultsFormat(ResultFormat.List);
        }
        return builder;
    }
}
