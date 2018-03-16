package org.apache.camel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks methods as being {@link ExchangePattern#InOnly}
 * for one way asynchronous invocation when using
 * to overload the default value which is {@link ExchangePattern#InOut} for request/reply if no annotations are used.
 *
 * This annotation can be added to individual methods or added to a class or interface to act as a default for all methods
 * within the class or interface.
 *
 * See the <a href="using-exchange-pattern-annotations.html">using exchange pattern annotations</a>
 * for more details on how the overloading rules work.
 *
 * @see org.apache.camel.ExchangePattern
 * @see org.apache.camel.Exchange#getPattern()
 * @see InOut
 *
 * @version $Revision: 711235 $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Pattern(ExchangePattern.InOnly)
public @interface InOnly {
}
