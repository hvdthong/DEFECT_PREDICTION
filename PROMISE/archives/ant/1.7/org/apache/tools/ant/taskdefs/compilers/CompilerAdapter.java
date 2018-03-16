package org.apache.tools.ant.taskdefs.compilers;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Javac;

/**
 * The interface that all compiler adapters must adhere to.
 *
 * <p>A compiler adapter is an adapter that interprets the javac's
 * parameters in preparation to be passed off to the compiler this
 * adapter represents.  As all the necessary values are stored in the
 * Javac task itself, the only thing all adapters need is the javac
 * task, the execute command and a parameterless constructor (for
 * reflection).</p>
 *
 * @since Ant 1.3
 */

public interface CompilerAdapter {

    /**
     * Sets the compiler attributes, which are stored in the Javac task.
     * @param attributes the compiler attributes
     */
    void setJavac(Javac attributes);

    /**
     * Executes the task.
     *
     * @return has the compilation been successful
     * @throws BuildException on error
     */
    boolean execute() throws BuildException;
}
