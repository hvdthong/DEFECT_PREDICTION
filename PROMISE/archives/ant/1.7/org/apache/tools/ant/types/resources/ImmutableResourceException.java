package org.apache.tools.ant.types.resources;

import java.io.IOException;

/**
 * Exception thrown when an attempt is made to get an OutputStream
 * from an immutable Resource.
 * @since Ant 1.7
 */
public class ImmutableResourceException extends IOException {

    /**
     * Default constructor.
     */
    public ImmutableResourceException() {
        super();
    }

    /**
     * Construct a new ImmutableResourceException with the specified message.
     * @param s the message String.
     */
    public ImmutableResourceException(String s) {
        super(s);
    }

}
