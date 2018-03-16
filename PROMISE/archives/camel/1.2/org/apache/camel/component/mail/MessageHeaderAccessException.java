package org.apache.camel.component.mail;

import javax.mail.MessagingException;

/**
 * @version $Revision:520964 $
 */
public class MessageHeaderAccessException extends RuntimeMailException {
    private static final long serialVersionUID = -3996286386119163309L;
    private final String propertyName;

    public MessageHeaderAccessException(String propertyName, MessagingException e) {
        super("Error accessing header: " + propertyName, e);
        this.propertyName = propertyName;
    }

    public String getPropertyName() {
        return propertyName;
    }
}
