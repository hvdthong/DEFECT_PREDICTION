package org.apache.camel.component.jms;

import javax.jms.JMSException;

/**
 * @version $Revision:520964 $
 */
public class MessagePropertyAccessException extends RuntimeJmsException {
    private static final long serialVersionUID = -3996286386119163309L;
    private String propertyName;

    public MessagePropertyAccessException(String propertyName, JMSException e) {
        super("Error accessing header: " + propertyName, e);
        this.propertyName = propertyName;
    }

    public String getPropertyName() {
        return propertyName;
    }
}
