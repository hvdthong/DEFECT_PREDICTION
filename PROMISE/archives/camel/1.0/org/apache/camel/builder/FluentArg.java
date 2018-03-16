package org.apache.camel.builder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 
 * Used to annotate the parameter of a {@see Fluent} method.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface FluentArg {
	String value();
	boolean attribute() default true;
	boolean element() default false;
    boolean reference() default false;
}
