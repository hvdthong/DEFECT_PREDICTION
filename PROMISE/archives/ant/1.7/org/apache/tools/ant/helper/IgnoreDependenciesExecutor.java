package org.apache.tools.ant.helper;

import java.util.Hashtable;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Executor;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Target;

/**
 * Target executor implementation that ignores dependencies. Runs each
 * target by calling <code>target.performTasks()</code> directly. If an
 * error occurs, behavior is determined by the Project's "keep-going" mode.
 * To be used when you know what you're doing.
 *
 * @since Ant 1.7.1
 */
public class IgnoreDependenciesExecutor implements Executor {

    private static final SingleCheckExecutor SUB_EXECUTOR = new SingleCheckExecutor();

    /** {@inheritDoc}. */
    public void executeTargets(Project project, String[] targetNames)
        throws BuildException {
        Hashtable targets = project.getTargets();
        BuildException thrownException = null;
        for (int i = 0; i < targetNames.length; i++) {
            try {
                Target t = (Target) targets.get(targetNames[i]);
                if (t == null) {
                  throw new BuildException("Unknown target " + targetNames[i]);
                }
                t.performTasks();
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
