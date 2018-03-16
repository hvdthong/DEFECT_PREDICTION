package org.apache.camel.builder.script;

import org.apache.camel.language.LanguageAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation for injection of PHP expressions
 *  into method parameters, fields or properties
 *
 * @version $Revision: 1.1 $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@LanguageAnnotation(language = "php")
public @interface PHP {
    public abstract String value();
}
