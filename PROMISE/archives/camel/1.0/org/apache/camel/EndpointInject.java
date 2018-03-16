package org.apache.camel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Used to indicate an injection point of an {@link Endpoint} or {@link Producer} in a POJO.
 * should be configured, or a name of an endpoint
 * which refers to a Spring bean name in your Spring ApplicationContext.
 *
 * @version $Revision: 523756 $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface EndpointInject {
    String uri() default "";
    String name() default "";
}
