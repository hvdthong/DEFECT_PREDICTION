package org.apache.camel.builder;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;

/**
 * A Factory of {@link Predicate} objects typically implemented by a builder such as
 * {@link org.apache.camel.builder.xml.XPathBuilder XPathBuilder}
 *
 * @version $Revision: 659760 $
 */
public interface PredicateFactory<E extends Exchange> {

    /**
     * Creates a predicate object
     *
     * @return the newly created expression object
     */
    Predicate<E> createPredicate();
}
