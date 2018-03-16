package org.apache.camel.component.mail;


/**
 * @version $Revision:520964 $
 */
public class MessageHeaderNamesAccessException extends RuntimeMailException {
    private static final long serialVersionUID = -6744171518099741324L;

    public MessageHeaderNamesAccessException(Throwable e) {
        super("Failed to acess the Mail message property names", e);
    }
}
