package org.apache.camel.impl;

import org.apache.camel.Predicate;
import org.apache.camel.Exchange;

/**
 * A useful base class for {@link Predicate} implementations
 *
 * @version $Revision: 1.1 $
 */
public abstract class PredicateSupport<E extends Exchange> implements Predicate<E> {

    public void assertMatches(String text, E exchange) {
        if (! matches(exchange)) {
            throw new AssertionError(assertionFailureMessage(exchange));
        }
    }

    protected String assertionFailureMessage(E exchange) {
        return this + " failed on " + exchange;
    }
}
