package org.apache.camel.processor.idempotent.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.UniqueConstraint;

/**
 * @version $Revision: 1.1 $
 */
@Entity
@UniqueConstraint(columnNames = {"processorName", "messageId" })
public class MessageProcessed {
    private Long id;
    private String messageId;
    private String processorName;

    
    @Override
    public String toString() {
        return "MessageProcessed[processorName: " + getProcessorName() + " messageId: " + getMessageId() + "]";
    }

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getProcessorName() {
        return processorName;
    }

    public void setProcessorName(String processorName) {
        this.processorName = processorName;
    }
}
