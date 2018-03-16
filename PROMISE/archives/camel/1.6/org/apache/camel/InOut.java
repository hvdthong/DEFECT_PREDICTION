package org.apache.camel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method as being {@link ExchangePattern#InOut} when a class or interface has been annotated with
 * {@link InOnly} when using
 *
 * This annotation is only intended to be used on methods which the class or interface has been annotated with
 * a default exchange pattern annotation such as {@link InOnly} or {@link Pattern}
 *
 * See the <a href="using-exchange-pattern-annotations.html">using exchange pattern annotations</a>
 * for more details on how the overloading rules work.
 *
 * @see org.apache.camel.ExchangePattern
 * @see org.apache.camel.Exchange#getPattern()
 * @see InOut
 *
 * @version $Revision: 689343 $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Pattern(ExchangePattern.InOut)
public @interface InOut {
}
