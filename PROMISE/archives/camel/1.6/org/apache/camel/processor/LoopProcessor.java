package org.apache.camel.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.util.ExchangeHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The processor which sends messages in a loop.
 *
 * @version $Revision: 699771 $
 */
public class LoopProcessor extends DelegateProcessor {
    public static final String PROP_ITER_COUNT = "CamelIterationCount";
    public static final String PROP_ITER_INDEX = "CamelIterationIndex";

    private static final Log LOG = LogFactory.getLog(LoopProcessor.class);

    private Expression<Exchange> expression;

    public LoopProcessor(Expression<Exchange> expression, Processor processor) {
        super(processor);
        this.expression = expression;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        String text = ExchangeHelper.convertToType(exchange, String.class, expression.evaluate(exchange));
        Integer value = ExchangeHelper.convertToType(exchange, Integer.class, text);
        if (value == null) {
            throw new RuntimeCamelException("Expression \"" + expression + "\" does not evaluate to an int.");
        }
        int count = value.intValue();
        exchange.setProperty(PROP_ITER_COUNT, count);
        for (int i = 0; i < count; i++) {
            LOG.debug("LoopProcessor: iteration #" + i);
            exchange.setProperty(PROP_ITER_INDEX, i);
            super.process(exchange);
        }
    }

    @Override
    public String toString() {
        return "Loop[for: " + expression + " times do: " + getProcessor() + "]";
    }

    public Expression<Exchange> getExpression() {
        return expression;
    }
}
