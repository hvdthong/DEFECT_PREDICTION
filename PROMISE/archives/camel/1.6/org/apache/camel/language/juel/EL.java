package org.apache.camel.language.juel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.camel.language.LanguageAnnotation;

/**
 * An annotation for injection of EL (JSP & JSF) expressions into method parameters, fields or properties
 *
 * @version $Revision: 640731 $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@LanguageAnnotation(language = "el")
public @interface EL {
    String value();
}
