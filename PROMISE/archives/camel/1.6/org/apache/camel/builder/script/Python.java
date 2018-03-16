package org.apache.camel.builder.script;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.camel.language.LanguageAnnotation;

/**
 * An annotation for injection of Python expressions
 *  into method parameters, fields or properties
 *
 * @version $Revision: 641680 $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@LanguageAnnotation(language = "python")
public @interface Python {
    String value();
}
