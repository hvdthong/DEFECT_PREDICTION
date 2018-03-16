package org.apache.camel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method as having a specific kind of {@link ExchangePattern} for use with
 * to overload the default value which is {@link ExchangePattern#InOut} for request/reply if no annotations are used.
 *
 * There are abbreviation annotations like {@link InOnly} or {@link InOut} which are typically used for
 * the common message exchange patterns. You could also add this annotation onto your own custom annotation to default
 * the message exchange pattern when your own annotation is added to a method
 * <a href="using-exchange-pattern-annotations.html">as in this example</a>.
 *
 * This annotation can be added to individual methods or added to a class or interface to act as a default for all methods
 * within the class or interface.
 *
 * See the <a href="using-exchange-pattern-annotations.html">using exchange pattern annotations</a>
 * for more details on how the overloading rules work.
 *
 * @see InOut
 * @see InOnly
 * @see ExchangePattern
 * @see Exchange#getPattern()
 *
 * @version $Revision: 711235 $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Pattern {

    /**
     * Specifies the exchange pattern to be used for this method
     */
    ExchangePattern value();
}
