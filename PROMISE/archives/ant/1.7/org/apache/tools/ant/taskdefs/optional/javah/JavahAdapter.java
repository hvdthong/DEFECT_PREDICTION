package org.apache.tools.ant.taskdefs.optional.javah;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.optional.Javah;

/**
 * Interface for different backend implementations of the Javah task.
 *
 * @since Ant 1.6.3
 */
public interface JavahAdapter {
    /**
     * Performs the actual compilation.
     * @param javah the calling javah task.
     * @return true if the compilation was successful.
     * @throws BuildException if there is an error.
     * @since Ant 1.6.3
     */
    boolean compile(Javah javah) throws BuildException;
}
