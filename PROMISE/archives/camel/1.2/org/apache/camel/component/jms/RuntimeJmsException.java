package org.apache.camel.component.jms;

import javax.jms.JMSException;

/**
 * @version $Revision:520964 $
 */
public class RuntimeJmsException extends RuntimeException {
    private static final long serialVersionUID = -2141493732308871761L;

    public RuntimeJmsException(String message, JMSException cause) {
        super(message, cause);
    }
}
