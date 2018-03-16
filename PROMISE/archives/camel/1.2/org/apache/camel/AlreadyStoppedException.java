package org.apache.camel;

/**
 * @version $Revision: $
 */
public class AlreadyStoppedException extends CamelException {

    public AlreadyStoppedException() {
        super("Already stopped");
    }
}
