package org.apache.camel.component.xquery;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.camel.language.LanguageAnnotation;
import org.apache.camel.language.NamespacePrefix;

/**
 * An annotation for injection of an XQuery expressions into a field, property, method or parameter when using
 *
 * @version $Revision: 641680 $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@LanguageAnnotation(language = "xquery", factory = XQueryAnnotationExpressionFactory.class)
public @interface XQuery {
    String value();

    NamespacePrefix[] namespaces() default {
}
