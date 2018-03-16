package org.apache.camel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to indicate a method on a POJO which is used as a {@link Consumer} of
 * {@link Exchange} instances to process {@link Message} instances.
 * 
 * endpoint should be configured, or a name of an endpoint which refers to a
 * Spring bean name in your Spring ApplicationContext.
 * 
 * @version $Revision: 630591 $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR })
public @interface MessageDriven {
    String uri() default "";

    String name() default "";
}
