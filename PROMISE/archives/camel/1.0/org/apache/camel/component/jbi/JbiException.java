package org.apache.camel.component.jbi;

import org.apache.camel.RuntimeCamelException;

/**
 * @version $Revision: 522517 $
 */
public class JbiException extends RuntimeCamelException {
    public JbiException(Throwable cause) {
        super(cause);
    }

    public JbiException(String message) {
        super(message);
    }

    public JbiException(String message, Throwable cause) {
        super(message, cause);
    }
}
