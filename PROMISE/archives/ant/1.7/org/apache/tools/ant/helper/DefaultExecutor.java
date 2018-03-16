package org.apache.tools.ant.helper;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Executor;
import org.apache.tools.ant.BuildException;

/**
 * Default Target executor implementation. Runs each target individually
 * (including all of its dependencies). If an error occurs, behavior is
 * determined by the Project's "keep-going" mode.
 * @since Ant 1.6.3
 */
public class DefaultExecutor implements Executor {

    private static final SingleCheckExecutor SUB_EXECUTOR = new SingleCheckExecutor();

    /** {@inheritDoc}. */
    public void executeTargets(Project project, String[] targetNames)
        throws BuildException {
        BuildException thrownException = null;
        for (int i = 0; i < targetNames.length; i++) {
            try {
                project.executeTarget(targetNames[i]);
            } catch (BuildException ex) {
                if (project.isKeepGoingMode()) {
                    thrownException = ex;
                } else {
                    throw ex;
                }
            }
        }
        if (thrownException != null) {
            throw thrownException;
        }
    }

    /** {@inheritDoc}. */
    public Executor getSubProjectExecutor() {
        return SUB_EXECUTOR;
    }

}
