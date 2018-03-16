package org.apache.camel.component.jms;

import javax.jms.JMSException;

/**
 * @version $Revision:520964 $
 */
public class MessagePropertyNamesAccessException extends RuntimeJmsException {
    private static final long serialVersionUID = -6744171518099741324L;

    public MessagePropertyNamesAccessException(JMSException e) {
        super("Failed to access the JMS message property names: " + e, e);
    }
}
