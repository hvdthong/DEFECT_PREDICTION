package org.apache.camel.component.jms.requestor;

import javax.jms.JMSException;
import javax.jms.Message;

/**
 * @version $Revision: 640731 $
 */
public interface ReplyHandler {
    /**
     * Processes the message, returning true if this is the last method of a lifecycle
     * so that the handler can be discarded
     */
    boolean handle(Message message) throws JMSException;
}
