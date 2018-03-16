package org.apache.tools.ant.taskdefs.compilers;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Javac;

/**
 * The interface that all compiler adapters must adher to.  
 *
 * <p>A compiler adapter is an adapter that interprets the javac's
 * parameters in preperation to be passed off to the compier this
 * adapter represents.  As all the necessary values are stored in the
 * Javac task itself, the only thing all adapters need is the javac
 * task, the execute command and a parameterless constructor (for
 * reflection).</p>
 *
 * @author Jay Dickon Glanville 
 *         <a href="mailto:jayglanville@home.com">jayglanville@home.com</a>
 * @since Ant 1.3
 */

public interface CompilerAdapter {

    /**
     * Sets the compiler attributes, which are stored in the Javac task.
     */
    void setJavac(Javac attributes);

    /**
     * Executes the task.
     *
     * @return has the compilation been successful
     */
    boolean execute() throws BuildException;
}
