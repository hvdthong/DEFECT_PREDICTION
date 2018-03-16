package org.apache.camel.language;

import org.apache.camel.component.bean.XPathAnnotationExpressionFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to inject an XPath expression into a field, property, method or parameter.
 *
 * @version $Revision: 1.1 $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@LanguageAnnotation(language = "xpath", factory = XPathAnnotationExpressionFactory.class)
public @interface XPath {
    public String value();

    public NamespacePrefix[] namespaces() default {
}
