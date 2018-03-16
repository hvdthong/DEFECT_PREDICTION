package org.apache.tools.ant.taskdefs.optional.jsp.compilers;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.optional.jsp.JspC;
import org.apache.tools.ant.taskdefs.optional.jsp.JspMangler;

/**
 * The interface that all jsp compiler adapters must adher to.
 *
 * <p>A compiler adapter is an adapter that interprets the jspc's
 * parameters in preperation to be passed off to the compier this
 * adapter represents.  As all the necessary values are stored in the
 * Jspc task itself, the only thing all adapters need is the jsp
 * task, the execute command and a parameterless constructor (for
 * reflection).</p>
 *
 */

public interface JspCompilerAdapter {

    /**
     * Sets the compiler attributes, which are stored in the Jspc task.
     */
    void setJspc(JspC attributes);

    /**
     * Executes the task.
     *
     * @return has the compilation been successful
     */
    boolean execute() throws BuildException;

    /**
     * @return an instance of the mangler this compiler uses
     */

    JspMangler createMangler();

    /**
     * ask if compiler can sort out its own dependencies
     * @return true if the compiler wants to do its own
     * depends
     */
    boolean implementsOwnDependencyChecking();
}
