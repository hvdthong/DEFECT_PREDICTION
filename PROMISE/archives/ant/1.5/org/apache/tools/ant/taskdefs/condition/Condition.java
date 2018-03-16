package org.apache.tools.ant.taskdefs.condition;

import org.apache.tools.ant.BuildException;

/**
 * Interface for conditions to use inside the &lt;condition&gt; task.
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 * @version $Revision: 274041 $
 */
public interface Condition {
    /**
     * Is this condition true?
     */
    boolean eval() throws BuildException;
}

