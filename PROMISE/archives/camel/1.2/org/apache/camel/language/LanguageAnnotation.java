package org.apache.camel.language;

import org.apache.camel.component.bean.DefaultAnnotationExpressionFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @version $Revision: 1.1 $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE })
public @interface LanguageAnnotation {
    public abstract String language();
    public abstract Class<?> factory() default DefaultAnnotationExpressionFactory.class;
}
