package org.apache.camel;

/**
 * Evaluates a binary <a
 * message exchange to support things like <a
 * any arbitrary Java expression.
 * 
 * @version $Revision: 630591 $
 */
public interface Predicate<E> {

    /**
     * Evaluates the predicate on the message exchange and returns true if this
     * exchange matches the predicate
     * 
     * @param exchange the message exchange
     * @return true if the predicate matches
     */
    boolean matches(E exchange);

    /**
     * Allows this predicate to be used nicely in testing to generate a nicely
     * formatted exception and message if this predicate does not match for the
     * given exchange.
     * 
     * @param text the description to use in the exception message
     * @param exchange the exchange to evaluate the expression on
     * @throws AssertionError if the predicate does not match
     */
    void assertMatches(String text, E exchange) throws AssertionError;

}
