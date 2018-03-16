package org.apache.tools.ant.taskdefs;


import java.io.*;

/**
 * Exception thrown indicating problems in a JAR Manifest
 *
 * @author <a href="mailto:conor@apache.org">Conor MacNeill</a>
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
