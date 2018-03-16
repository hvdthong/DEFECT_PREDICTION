package org.apache.camel.component.jms;

import javax.jms.JMSException;

/**
 * @version $Revision: 1.1 $
 */
public class MessageJMSPropertyAccessException extends RuntimeJmsException {
    private static final long serialVersionUID = -6744171518099741324L;

    public MessageJMSPropertyAccessException(JMSException e) {
        super("Failed to access a JMS property: " + e, e);
    }
}
