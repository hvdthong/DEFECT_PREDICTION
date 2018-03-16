package org.apache.camel.builder;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Processor;

/**
 * A builder of a number of different {@link Processor} implementations
 *
 * @version $Revision: 1.1 $
 */
public class ProcessorBuilder {

    /**
     * Creates a processor which sets the body of the IN message to the value of the expression
     */
    public static Processor setBody(final Expression expression) {
        return new Processor() {
            public void process(Exchange exchange) {
                Object newBody = expression.evaluate(exchange);
                exchange.getIn().setBody(newBody);
            }

            @Override
            public String toString() {
                return "setBody(" + expression + ")";
            }
        };
    }

    /**
     * Creates a processor which sets the body of the IN message to the value of the expression
     */
    public static Processor setOutBody(final Expression expression) {
        return new Processor() {
            public void process(Exchange exchange) {
                Object newBody = expression.evaluate(exchange);
                exchange.getOut().setBody(newBody);
            }

            @Override
            public String toString() {
                return "setOutBody(" + expression + ")";
            }
        };
    }

    /**
     * Sets the header on the IN message
     */
    public static Processor setHeader(final String name, final Expression expression) {
        return new Processor() {
            public void process(Exchange exchange) {
                Object value = expression.evaluate(exchange);
                exchange.getIn().setHeader(name, value);
            }

            @Override
            public String toString() {
                return "setHeader(" + name + ", " + expression + ")";
            }
        };
    }

    /**
     * Sets the header on the OUT message
     */
    public static Processor setOutHeader(final String name, final Expression expression) {
        return new Processor() {
            public void process(Exchange exchange) {
                Object value = expression.evaluate(exchange);
                exchange.getOut().setHeader(name, value);
            }

            @Override
            public String toString() {
                return "setOutHeader(" + name + ", " + expression + ")";
            }
        };
    }

    /**
     * Sets the property on the exchange
     */
    public static Processor setProperty(final String name, final Expression expression) {
        return new Processor() {
            public void process(Exchange exchange) {
                Object value = expression.evaluate(exchange);
                exchange.setProperty(name, value);
            }

            @Override
            public String toString() {
                return "setProperty(" + name + ", " + expression + ")";
            }
        };
    }
}
