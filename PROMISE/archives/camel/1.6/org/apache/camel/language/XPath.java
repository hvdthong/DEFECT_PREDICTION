package org.apache.camel.language;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.camel.component.bean.XPathAnnotationExpressionFactory;

/**
 * Used to inject an XPath expression into a field, property, method or parameter when using
 *
 * @version $Revision: 640438 $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@LanguageAnnotation(language = "xpath", factory = XPathAnnotationExpressionFactory.class)
public @interface XPath {
    String value();

    NamespacePrefix[] namespaces() default {
}
