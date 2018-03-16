package org.apache.camel.builder;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;

/**
 * A helper class for including portions of the
 *
 * @version $Revision: 1.1 $
 */
public class Builder {

    /**
     * Returns a constant expression
     */
    public static <E extends Exchange> ValueBuilder<E> constant(Object value) {
        Expression<E> expression = ExpressionBuilder.constantExpression(value);
        return new ValueBuilder<E>(expression);
    }

    /**
     * Returns a predicate and value builder for headers on an exchange
     */
    public static <E extends Exchange> ValueBuilder<E> header(@FluentArg("name") String name) {
        Expression<E> expression = ExpressionBuilder.headerExpression(name);
        return new ValueBuilder<E>(expression);
    }

    /**
     * Returns a predicate and value builder for the inbound body on an exchange
     */
    public static <E extends Exchange> ValueBuilder<E> body() {
        Expression<E> expression = ExpressionBuilder.bodyExpression();
        return new ValueBuilder<E>(expression);
    }

    /**
     * Returns a predicate and value builder for the inbound message body as a specific type
     */
    public static <E extends Exchange, T> ValueBuilder<E> bodyAs( Class<T> type) {
        Expression<E> expression = ExpressionBuilder.<E, T>bodyExpression(type);
        return new ValueBuilder<E>(expression);
    }

    /**
     * Returns a predicate and value builder for the outbound body on an exchange
     */
    public static <E extends Exchange> ValueBuilder<E> outBody() {
        Expression<E> expression = ExpressionBuilder.bodyExpression();
        return new ValueBuilder<E>(expression);
    }

    /**
     * Returns a predicate and value builder for the outbound message body as a specific type
     */
    public static <E extends Exchange, T> ValueBuilder<E> outBody(Class<T> type) {
        Expression<E> expression = ExpressionBuilder.<E, T>bodyExpression(type);
        return new ValueBuilder<E>(expression);
    }


    /**
     * Returns an expression for the given system property
     */
    public static <E extends Exchange> ValueBuilder<E> systemProperty(final String name) {
        return systemProperty(name, null);
    }

    /**
     * Returns an expression for the given system property
     */
    public static <E extends Exchange> ValueBuilder<E> systemProperty(final String name, final String defaultValue) {
        return new ValueBuilder<E>(ExpressionBuilder.<E>systemProperty(name, defaultValue));
    }
}
