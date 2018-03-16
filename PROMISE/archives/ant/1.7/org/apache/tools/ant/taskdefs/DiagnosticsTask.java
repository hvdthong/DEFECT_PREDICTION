package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Diagnostics;
import org.apache.tools.ant.Task;

/**
 * This is a task that hands off work to the Diagnostics module.
 * It lets you run diagnostics in an IDE.
 */
public class DiagnosticsTask extends Task {

    private static final String[] ARGS = new String[0];

    /**
     * Execute the task.
     * This delegates to the Diagnostics class.
     * @throws BuildException on error.
     */
    public void execute() throws BuildException {
        Diagnostics.main(ARGS);
    }

}
