package org.apache.camel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Subscribes a method to an {@link Endpoint} either via its
 * which is then resolved in a registry such as the Spring Application Context.
 *
 * When a message {@link Exchange} is received from the {@link Endpoint} then the
 * mechanism is used to map the incoming {@link Message} to the method parameters.
 * 
 * @version $Revision: 711235 $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR })
public @interface Consume {
    String uri() default "";

    String ref() default "";
}
