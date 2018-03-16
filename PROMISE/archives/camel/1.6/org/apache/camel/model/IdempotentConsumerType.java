package org.apache.camel.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.processor.idempotent.IdempotentConsumer;
import org.apache.camel.processor.idempotent.MessageIdRepository;
import org.apache.camel.spi.RouteContext;

/**
 * Represents an XML &lt;idempotentConsumer/&gt; element
 *
 * @version $Revision: 705880 $
 */
@XmlRootElement(name = "idempotentConsumer")
@XmlAccessorType(XmlAccessType.FIELD)
public class IdempotentConsumerType extends ExpressionNode {
    @XmlAttribute
    private String messageIdRepositoryRef;
    @XmlTransient
    private MessageIdRepository messageIdRepository;

    public IdempotentConsumerType() {
    }

    public IdempotentConsumerType(Expression messageIdExpression, MessageIdRepository messageIdRepository) {
        super(messageIdExpression);
        this.messageIdRepository = messageIdRepository;
    }

    @Override
    public String toString() {
        return "IdempotentConsumer[" + getExpression() + " -> " + getOutputs() + "]";
    }

    @Override
    public String getShortName() {
        return "idempotentConsumer";
    }

    public String getMessageIdRepositoryRef() {
        return messageIdRepositoryRef;
    }

    public void setMessageIdRepositoryRef(String messageIdRepositoryRef) {
        this.messageIdRepositoryRef = messageIdRepositoryRef;
    }

    public MessageIdRepository getMessageIdRepository() {
        return messageIdRepository;
    }

    public void setMessageIdRepository(MessageIdRepository messageIdRepository) {
        this.messageIdRepository = messageIdRepository;
    }

    @Override
    public Processor createProcessor(RouteContext routeContext) throws Exception {
        Processor childProcessor = routeContext.createProcessor(this);
        MessageIdRepository messageIdRepository = resolveMessageIdRepository(routeContext);
        return new IdempotentConsumer(getExpression().createExpression(routeContext), messageIdRepository,
                                      childProcessor);
    }

    public MessageIdRepository resolveMessageIdRepository(RouteContext routeContext) {
        if (messageIdRepository == null) {
            messageIdRepository = routeContext.lookup(messageIdRepositoryRef, MessageIdRepository.class);
        }
        return messageIdRepository;
    }
}
