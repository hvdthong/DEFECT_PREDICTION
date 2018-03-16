package org.apache.tools.ant.types.selectors.modifiedselector;


import java.io.File;


/**
 * The <i>Algorithm</i> defines how a value for a file is computed.
 * It must be sure that multiple calls for the same file results in the
 * same value.
 * The implementing class should implement a useful toString() method.
 *
 * @version 2003-09-13
 * @since  Ant 1.6
 */
public interface Algorithm {

    /**
     * Checks its prerequisites.
     * @return <i>true</i> if all is ok, otherwise <i>false</i>.
     */
    boolean isValid();

    /**
     * Get the value for a file.
     * @param file    File object for which the value should be evaluated.
     * @return        The value for that file
     */
    String getValue(File file);
}
