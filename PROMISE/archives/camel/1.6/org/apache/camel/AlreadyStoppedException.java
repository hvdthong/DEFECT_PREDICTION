package org.apache.camel;

/**
 * Exception thrown in situations when a {@link Service} has already been stopped.
 *
 * @version $Revision: 659422 $
 */
public class AlreadyStoppedException extends CamelException {

    public AlreadyStoppedException() {
        super("Already stopped");
    }
}
