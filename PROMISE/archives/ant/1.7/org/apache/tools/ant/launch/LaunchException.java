package org.apache.tools.ant.launch;

/**
 * Signals an error condition during launching
 *
 * @since Ant 1.6
 */
public class LaunchException extends Exception {

    /**
     * Constructs an exception with the given descriptive message.
     *
     * @param message A description of or information about the exception.
     *            Should not be <code>null</code>.
     */
    public LaunchException(String message) {
        super(message);
    }

}
