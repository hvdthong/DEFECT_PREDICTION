package org.apache.camel.language;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.camel.component.bean.DefaultAnnotationExpressionFactory;

/**
 * Base annotation for languages.
 *
 * @version $Revision: 659760 $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE })
public @interface LanguageAnnotation {
    String language();
    Class<?> factory() default DefaultAnnotationExpressionFactory.class;
}
