package org.apache.camel.processor.idempotent;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.RuntimeCamelException;

/**
 * An exception thrown if no message ID could be found on a message which is to be used with the
 *
 * @version $Revision: 1.1 $
 */
public class NoMessageIdException extends RuntimeCamelException {
    private final Exchange exchange;
    private final Expression expression;

    public NoMessageIdException(Exchange exchange, Expression expression) {
        super("No message ID could be found using expression: " + expression + " on message exchange: " + exchange);
        this.exchange = exchange;
        this.expression = expression;
    }

    /**
     * The exchange which caused this failure
     */
    public Exchange getExchange() {
        return exchange;
    }

    /**
     * The expression which was used
     */
    public Expression getExpression() {
        return expression;
    }
}
