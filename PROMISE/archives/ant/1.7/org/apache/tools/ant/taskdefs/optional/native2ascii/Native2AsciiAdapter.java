package org.apache.tools.ant.taskdefs.optional.native2ascii;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.optional.Native2Ascii;

/**
 * Interface for an adapter to a native2ascii implementation.
 *
 * @since Ant 1.6.3
 */
public interface Native2AsciiAdapter {
    /**
     * Convert the encoding of srcFile writing to destFile.
     *
     * @param args Task that holds command line arguments and allows
     * the implementation to send messages to Ant's logging system
     * @param srcFile the source to convert
     * @param destFile where to send output to
     * @return whether the conversion has been successful.
     * @throws BuildException if there was a problem.
     */
    boolean convert(Native2Ascii args, File srcFile, File destFile)
        throws BuildException;
}
