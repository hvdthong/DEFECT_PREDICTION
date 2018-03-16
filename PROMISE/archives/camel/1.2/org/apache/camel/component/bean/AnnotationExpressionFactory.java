package org.apache.camel.component.bean;

import org.apache.camel.Expression;
import org.apache.camel.CamelContext;
import org.apache.camel.language.LanguageAnnotation;

import java.lang.annotation.Annotation;

/**
 * A factory which creates an {@link Expression} object from an annotation on a field, property or method parameter
 * of a specified type.
 *
 * @version $Revision: 1.1 $
 */
public interface AnnotationExpressionFactory {

    Expression createExpression(CamelContext camelContext, Annotation annotation, LanguageAnnotation languageAnnotation, Class expressionReturnType);
}
