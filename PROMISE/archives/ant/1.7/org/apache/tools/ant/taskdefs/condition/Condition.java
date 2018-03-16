package org.apache.tools.ant.taskdefs.condition;

import org.apache.tools.ant.BuildException;

/**
 * Interface for conditions to use inside the &lt;condition&gt; task.
 *
 */
public interface Condition {
    /**
     * Is this condition true?
     * @return true if the condition is true
     * @exception BuildException if an error occurs
     */
    boolean eval() throws BuildException;
}

