package org.apache.camel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a field or property as being a producer to an {@link org.apache.camel.Endpoint} either via its
 * which is then resolved in a registry such as the Spring Application Context.
 *
 * Methods invoked on the producer object are then converted to a message {@link org.apache.camel.Exchange} via the
 * mechanism.
 *
 * @see InOnly
 *
 * @version $Revision: 689343 $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR })
public @interface Produce {
    String uri() default "";

    String ref() default "";
}
