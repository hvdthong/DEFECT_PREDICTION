package org.apache.camel.bam.processor;

import org.apache.camel.CamelExchangeException;
import org.apache.camel.Exchange;

/**
 * An exception thrown if no correlation key could be found for a message
 * exchange preventing any particular orchestration or
 *
 * @version $Revision: 630591 $
 */
public class NoCorrelationKeyException extends CamelExchangeException {
    private final BamProcessorSupport processor;

    public NoCorrelationKeyException(BamProcessorSupport processor, Exchange exchange) {
        super("No correlation key could be found for " + processor.getCorrelationKeyExpression(), exchange);
        this.processor = processor;
    }

    public BamProcessorSupport getProcessor() {
        return processor;
    }
}
