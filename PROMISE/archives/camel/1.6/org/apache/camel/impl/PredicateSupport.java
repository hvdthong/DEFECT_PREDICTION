package org.apache.camel.impl;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;

/**
 * A useful base class for {@link Predicate} implementations
 *
 * @version $Revision: 630591 $
 */
public abstract class PredicateSupport<E extends Exchange> implements Predicate<E> {

    public void assertMatches(String text, E exchange) {
        if (!matches(exchange)) {
            throw new AssertionError(assertionFailureMessage(exchange)  + " on " + exchange);
        }
    }

    protected String assertionFailureMessage(E exchange) {
        return toString();
    }
}
