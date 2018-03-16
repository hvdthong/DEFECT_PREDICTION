package org.apache.camel.builder.script;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.camel.language.LanguageAnnotation;

/**
 * An annotation for injection of BeanShell expressions
 *  into method parameters, fields or properties
 *
 * @deprecated will be removed in Camel 2.0
 * @version $Revision: 700514 $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@LanguageAnnotation(language = "beanshell")
@Deprecated
public @interface BeanShell {
    String value();
}
