package org.apache.camel.spi;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Predicate;

/**
 * Represents a language to be used for {@link Expression} or {@link Predicate} instances
 *
 * @version $Revision: 630568 $
 */
public interface Language {

    Predicate<Exchange> createPredicate(String expression);

    Expression<Exchange> createExpression(String expression);
}
