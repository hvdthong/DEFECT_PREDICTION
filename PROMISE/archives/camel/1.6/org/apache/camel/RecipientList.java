package org.apache.camel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that this method is to be used as a 
 * to one or more endpoints.
 *
 * When a message {@link org.apache.camel.Exchange} is received from an {@link org.apache.camel.Endpoint} then the
 * mechanism is used to map the incoming {@link org.apache.camel.Message} to the method parameters.
 *
 * The return value of the method is then converted to either a {@link java.util.Collection} or array of objects where each
 * element is converted to an {@link Endpoint} or a {@link String}, or if it is not a collection/array then it is converted
 * to an {@link Endpoint} or {@link String}.
 *
 * Then for each endpoint or URI the message is forwarded a separate copy.
 *
 * @version $Revision: 711235 $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR })
public @interface RecipientList {
}
