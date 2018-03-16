package org.apache.camel;

/**
 * provides a plugin strategy for evaluating expressions on a message exchange to support things like
 * as any arbitrary Java expression.
 *
 *
 * @version $Revision: 630591 $
 */
public interface Expression<E extends Exchange> {

    /**
     * Returns the value of the expression on the given exchange
     *
     * @param exchange the message exchange on which to evaluate the expression
     * @return the value of the expression
     */
    Object evaluate(E exchange);
}
