package org.apache.camel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.camel.spi.Registry;

/**
 * Used to indicate an injection point of an {@link Endpoint}, {@link Producer} or
 * {@link ProducerTemplate} into a POJO.
 *
 * can be specified on this annotation, or a name can be specified which is resolved in the
 * {@link Registry} such as in your Spring ApplicationContext.
 *
 * If no name or uri is specified then the name is defaulted from the field, property or method name.
 *
 * @version $Revision: 662986 $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR })
public @interface EndpointInject {
    String uri() default "";
    String name() default "";
}
