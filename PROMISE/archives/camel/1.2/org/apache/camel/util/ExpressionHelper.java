package org.apache.camel.util;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;

/**
 * A collection of helper methods for working with expressions.
 * 
 * @version $Revision: 1.1 $
 */
public class ExpressionHelper {

    /**
     * Utility classes should not have a public constructor.
     */
    private ExpressionHelper() {        
    }

    /**
     * Evaluates the given expression on the exchange as a String value
     * 
     * @param expression the expression to evaluate
     * @param exchange the exchange to use to evaluate the expression
     * @return the result of the evaluation as a string.
     */
    public static <E extends Exchange> String evaluateAsString(Expression<E> expression, E exchange) {
        return evaluateAsType(expression, exchange, String.class);
    }

    /**
     * Evaluates the given expression on the exchange, converting the result to
     * the given type
     * 
     * @param expression the expression to evaluate
     * @param exchange the exchange to use to evaluate the expression
     * @param resultType the type of the result that is required
     * @return the result of the evaluation as the specified type.
     */
    public static <T, E extends Exchange> T evaluateAsType(Expression<E> expression, E exchange,
                                                           Class<T> resultType) {
        Object value = expression.evaluate(exchange);
        return exchange.getContext().getTypeConverter().convertTo(resultType, value);
    }
}
