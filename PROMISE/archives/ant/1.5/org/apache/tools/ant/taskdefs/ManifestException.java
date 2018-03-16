package org.apache.tools.ant.taskdefs;

/**
 * Exception thrown indicating problems in a JAR Manifest
 *
 * @author Conor MacNeill
 * @since Ant 1.4
 */
public class ManifestException extends Exception {

    /**
     * Constructs an exception with the given descriptive message.
     * @param msg Description of or information about the exception.
     */
    public ManifestException(String msg) {
        super(msg);
    }
}
