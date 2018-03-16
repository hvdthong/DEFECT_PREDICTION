package org.apache.camel.impl;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Predicate;
import org.apache.camel.util.ObjectHelper;

/**
 * A useful base class for {@link Predicate} and {@link Expression} implementations
 *
 * @version $Revision: 663018 $
 */
public abstract class ExpressionSupport<E extends Exchange> implements Expression<E> , Predicate<E> {

    public boolean matches(E exchange) {
        Object value = evaluate(exchange);
        return ObjectHelper.evaluateValuePredicate(value);
    }

    public void assertMatches(String text, E exchange) {
        if (!matches(exchange)) {
            throw new AssertionError(text + " " + assertionFailureMessage(exchange) + " for exchange: " + exchange);
        }
    }

    protected abstract String assertionFailureMessage(E exchange);
}
