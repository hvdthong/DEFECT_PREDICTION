package org.apache.camel.builder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 
 * Used to annotate fluent API methods.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Fluent {
	
	/**
	 * Used to change the default element name of the action.
	 * @return
	 */
	String value() default "";
	
	/** 
	 * Should nested elements be evaluated against the current builder.
	 * @return
	 */
	boolean nestedActions() default false;

	/** 
	 * Should this method be called at the end of the evaluating the 
	 * element.
	 * @return
	 */
	boolean callOnElementEnd() default false;
}
