package org.apache.camel.component.xmpp;

import org.jivesoftware.smack.XMPPException;

/**
 * A runtime exception thrown if sending or receiving from XMPP fails
 *
 * @version $Revision:520964 $
 */
public class RuntimeXmppException extends RuntimeException {
    private static final long serialVersionUID = -2141493732308871761L;

    public RuntimeXmppException(XMPPException cause) {
        super(cause);
    }
    public RuntimeXmppException(String message, XMPPException cause) {
        super(message, cause);
    }

}
