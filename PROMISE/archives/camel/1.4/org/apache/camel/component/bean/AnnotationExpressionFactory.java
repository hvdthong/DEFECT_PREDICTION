package org.apache.camel.component.bean;

import java.lang.annotation.Annotation;

import org.apache.camel.CamelContext;
import org.apache.camel.Expression;
import org.apache.camel.language.LanguageAnnotation;

/**
 * A factory which creates an {@link Expression} object from an annotation on a field, property or method parameter
 * of a specified type.
 *
 * @version $Revision: 640438 $
 */
public interface AnnotationExpressionFactory {

    Expression createExpression(CamelContext camelContext, Annotation annotation, LanguageAnnotation languageAnnotation, Class expressionReturnType);
}
