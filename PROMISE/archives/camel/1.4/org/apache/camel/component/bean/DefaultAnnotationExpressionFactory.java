package org.apache.camel.component.bean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.apache.camel.CamelContext;
import org.apache.camel.Expression;
import org.apache.camel.language.LanguageAnnotation;
import org.apache.camel.spi.Language;
import org.apache.camel.util.ObjectHelper;

/**
 * Default implementation of the {@link AnnotationExpressionFactory}.
 *
 * @version $Revision: 660275 $
 */
public class DefaultAnnotationExpressionFactory implements AnnotationExpressionFactory {

    public Expression createExpression(CamelContext camelContext, Annotation annotation, LanguageAnnotation languageAnnotation, Class expressionReturnType) {
        String languageName = languageAnnotation.language();
        if (languageName == null) {
            throw new IllegalArgumentException("Cannot determine the language from the annotation: " + annotation);
        }
        Language language = camelContext.resolveLanguage(languageName);
        if (language == null) {
            throw new IllegalArgumentException("Cannot find the language: " + languageName + " on the classpath");
        }
        String expression = getExpressionFromAnnotation(annotation);
        return language.createExpression(expression);
    }

    protected String getExpressionFromAnnotation(Annotation annotation) {
        try {
            Method method = annotation.getClass().getMethod("value");
            Object value = ObjectHelper.invokeMethod(method, annotation);
            if (value == null) {
                throw new IllegalArgumentException("Cannot determine the expression from the annotation: " + annotation);
            }
            return value.toString();
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Cannot determine the expression of the annotation: " + annotation + " as it does not have an value() method");
        }
    }
}
