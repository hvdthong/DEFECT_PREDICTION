package org.apache.camel.bam.processor;

import org.apache.camel.CamelException;
import org.apache.camel.Exchange;

/**
 * An exception thrown if no correlation key could be found for a message
 * exchange preventing any particular orchestration or
 *
 * @version $Revision: $
 */
public class NoCorrelationKeyException extends CamelException {
    private BamProcessorSupport processor;
    private Exchange exchange;

    public NoCorrelationKeyException(BamProcessorSupport processor, Exchange exchange) {
        super("No correlation key could be found for " + processor.getCorrelationKeyExpression()
                + " on " + exchange);

        this.processor = processor;
        this.exchange = exchange;
    }

    public Exchange getExchange() {
        return exchange;
    }

    public BamProcessorSupport getProcessor() {
        return processor;
    }
}
