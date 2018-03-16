package org.apache.camel.builder.sql;

import org.apache.camel.language.LanguageAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation for injection of SQL expressions into method parameters, fields or properties
 *
 * @version $Revision: 1.1 $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@LanguageAnnotation(language = "sql")
public @interface SQL {
    public abstract String value();
}
