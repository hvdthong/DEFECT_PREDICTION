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
     * Utility classes should not have a public constructor.
     */
    private ProcessorBuilder() {        
    }

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
     * Creates a processor which sets the body of the OUT message to the value of the expression
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
     * Creates a processor which sets the body of the FAULT message to the value of the expression
     */
    public static Processor setFaultBody(final Expression expression) {
        return new Processor() {
            public void process(Exchange exchange) {
                Object newBody = expression.evaluate(exchange);
                exchange.getFault().setBody(newBody);
            }

            @Override
            public String toString() {
                return "setFaultBody(" + expression + ")";
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
     * Sets the header on the FAULT message
     */
    public static Processor setFaultHeader(final String name, final Expression expression) {
        return new Processor() {
            public void process(Exchange exchange) {
                Object value = expression.evaluate(exchange);
                exchange.getFault().setHeader(name, value);
            }

            @Override
            public String toString() {
                return "setFaultHeader(" + name + ", " + expression + ")";
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

    /**
     * Removes the header on the IN message
     */
    public static Processor removeHeader(final String name) {
        return new Processor() {
            public void process(Exchange exchange) {
                exchange.getIn().removeHeader(name);
            }

            @Override
            public String toString() {
                return "removeHeader(" + name +  ")";
            }
        };
    }

    /**
     * Removes the header on the OUT message
     */
    public static Processor removeOutHeader(final String name) {
        return new Processor() {
            public void process(Exchange exchange) {
                exchange.getOut().removeHeader(name);
            }

            @Override
            public String toString() {
                return "removeOutHeader(" + name +  ")";
            }
        };
    }

    /**
     * Removes the header on the FAULT message
     */
    public static Processor removeFaultHeader(final String name) {
        return new Processor() {
            public void process(Exchange exchange) {
                exchange.getFault().removeHeader(name);
            }

            @Override
            public String toString() {
                return "removeFaultHeader(" + name +  ")";
            }
        };
    }

    /**
     * Removes the property on the exchange
     */
    public static Processor removeProperty(final String name) {
        return new Processor() {
            public void process(Exchange exchange) {
                exchange.removeProperty(name);
            }

            @Override
            public String toString() {
                return "removeProperty(" + name +  ")";
            }
        };
    }
}