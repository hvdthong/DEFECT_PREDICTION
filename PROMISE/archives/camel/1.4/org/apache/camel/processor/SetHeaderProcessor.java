package org.apache.camel.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A processor which sets the header on the IN message
 */
public class SetHeaderProcessor extends DelegateProcessor implements Processor {
    private static final transient Log LOG = LogFactory.getLog(SetHeaderProcessor.class);
    private String name;
    private Expression expression;

    public SetHeaderProcessor(String name, Expression expression) {
        this.name = name;
        this.expression = expression;
    }

    public SetHeaderProcessor(String name, Expression expression,
            Processor childProcessor) {
        super(childProcessor);
        this.name = name;
        this.expression = expression;
    }

    public void process(Exchange exchange) throws Exception {
        Object value = expression.evaluate(exchange);
        if (value == null) {
            LOG.warn("Expression: " + expression
                    + " on exchange: " + exchange + " evaluated to null.");
        }
        exchange.getIn().setHeader(name, value);
        super.process(exchange);
    }

    @Override
    public String toString() {
        return "setHeader(" + name + ", " + expression + ")";
    }
}
