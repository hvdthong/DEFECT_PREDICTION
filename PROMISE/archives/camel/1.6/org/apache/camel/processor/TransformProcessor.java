package org.apache.camel.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A processor which sets the body on the OUT message with an expression
 */
public class TransformProcessor extends DelegateProcessor implements Processor {
    private static final transient Log LOG = LogFactory.getLog(TransformProcessor.class);
    private Expression expression;

    public TransformProcessor(Expression expression) {
        this.expression = expression;
    }

    public TransformProcessor(Expression expression, Processor childProcessor) {
        super(childProcessor);
        this.expression = expression;
    }

    public void process(Exchange exchange) throws Exception {
        Object newBody = expression.evaluate(exchange);
        exchange.getOut().setBody(newBody);

        exchange.getOut().getHeaders().putAll(exchange.getIn().getHeaders());
        
        super.process(exchange);
    }

    @Override
    public String toString() {
        return "transform(" + expression + "," + processor + ")";
    }
}
