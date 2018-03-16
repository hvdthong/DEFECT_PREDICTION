package org.apache.camel.spi;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Predicate;

/**
 * Represents a language to be used for {@link Expression} or {@link Predicate} instances
 *
 * @version $Revision: 688279 $
 */
public interface Language {

    /**
     * Creates a predicate based on the given string input
     *
     * @param expression  the expression
     * @return the created predicate
     */
    Predicate<Exchange> createPredicate(String expression);

    /**
     * Creates an expression based on the given string input
     *
     * @param expression  the expression as a string input
     * @return the created expression
     */
    Expression<Exchange> createExpression(String expression);
}
