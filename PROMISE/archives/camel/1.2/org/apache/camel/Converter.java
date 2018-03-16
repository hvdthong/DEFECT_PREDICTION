package org.apache.camel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation used to mark classes and methods to indicate code capable of
 * converting from a type to another type which are then auto-discovered using
 * Conversion Support</a>
 * 
 * @version $Revision: 563607 $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD })
public @interface Converter {
}
