package org.apache.camel.builder;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Expression;
import org.apache.camel.processor.idempotent.IdempotentConsumer;
import org.apache.camel.processor.idempotent.MessageIdRepository;

/**
 * A builder of an {@link IdempotentConsumer}
 *
 * @version $Revision: 1.1 $
 */
public class IdempotentConsumerBuilder extends FromBuilder implements ProcessorFactory {
    private final Expression messageIdExpression;
    private final MessageIdRepository messageIdRegistry;

    public IdempotentConsumerBuilder(FromBuilder fromBuilder, Expression messageIdExpression, MessageIdRepository messageIdRegistry) {
        super(fromBuilder);
        this.messageIdRegistry = messageIdRegistry;
        this.messageIdExpression = messageIdExpression;
    }

    public MessageIdRepository getMessageIdRegistry() {
        return messageIdRegistry;
    }

    @Override
    protected Processor wrapInErrorHandler(Processor processor) throws Exception {
        return processor;
    }

    @Override
    protected Processor wrapProcessor(Processor processor) {
        return new IdempotentConsumer(messageIdExpression, messageIdRegistry, processor);
    }
}
