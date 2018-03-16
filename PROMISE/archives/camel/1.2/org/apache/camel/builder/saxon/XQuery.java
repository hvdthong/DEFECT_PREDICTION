package org.apache.camel.builder.saxon;

import org.apache.camel.language.LanguageAnnotation;
import org.apache.camel.language.NamespacePrefix;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation for injection of XQuery expressions
 * into method parameters, fields or properties
 *
 * @version $Revision: 1.1 $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@LanguageAnnotation(language = "xquery", factory = XQueryAnnotationExpressionFactory.class)
public @interface XQuery {
    public String value();

    public NamespacePrefix[] namespaces() default {
}
