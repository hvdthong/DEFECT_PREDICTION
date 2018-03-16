package org.apache.tools.ant.taskdefs;

/**
 * Exception thrown indicating problems in a JAR Manifest
 *
 * @since Ant 1.4
 */
public class ManifestException extends Exception {

    private static final long serialVersionUID = 7685634200457515207L;

    /**
     * Constructs an exception with the given descriptive message.
     * @param msg Description of or information about the exception.
     */
    public ManifestException(String msg) {
        super(msg);
    }
}
