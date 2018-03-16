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
     *
     * @since Ant 1.6.3
     */
    boolean compile(Javah javah) throws BuildException;
}
