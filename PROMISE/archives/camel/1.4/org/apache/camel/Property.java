package org.apache.camel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a parameter as being an injection point of a property of an {@link Exchange}
 *
 * @see Exchange#getProperty(String)  
 * @version $Revision: 630591 $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER })
public @interface Property {
    String name();
}
