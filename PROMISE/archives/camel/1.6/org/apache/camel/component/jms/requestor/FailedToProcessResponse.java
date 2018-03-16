package org.apache.camel.component.jms.requestor;

import javax.jms.JMSException;
import javax.jms.Message;

import org.apache.camel.RuntimeCamelException;

/**
 * An exception thrown if a response message from an InOut could not be processed
 *
 * @version $Revision: 642772 $
 */
public class FailedToProcessResponse extends RuntimeCamelException {
    private final Message response;

    public FailedToProcessResponse(Message response, JMSException e) {
        super("Failed to process response: " + e + ". Message: " + response, e);
        this.response = response;
    }

    /**
     * The response message which caused the exception
     */
    public Message getResponse() {
        return response;
    }
}
