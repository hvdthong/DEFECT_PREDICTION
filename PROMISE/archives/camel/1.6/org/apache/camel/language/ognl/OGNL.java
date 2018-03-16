package org.apache.camel.language.ognl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.camel.language.LanguageAnnotation;

/**
 *  into method parameters, fields or properties
 *
 * @version $Revision: 641674 $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@LanguageAnnotation(language = "ognl")
public @interface OGNL {
    String value();
}
