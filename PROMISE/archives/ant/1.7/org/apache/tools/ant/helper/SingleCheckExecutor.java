package org.apache.tools.ant.helper;


import org.apache.tools.ant.Project;
import org.apache.tools.ant.Executor;
import org.apache.tools.ant.BuildException;


/**
 * "Single-check" Target executor implementation.
 * Differs from {@link DefaultExecutor} in that the dependencies for all
 * targets are computed together, so that shared dependencies are run just once.
 * @since Ant 1.6.3
 */
public class SingleCheckExecutor implements Executor {

    /** {@inheritDoc}. */
    public void executeTargets(Project project, String[] targetNames)
        throws BuildException {
            project.executeSortedTargets(
                project.topoSort(targetNames, project.getTargets(), false));
    }

    /** {@inheritDoc}. */
    public Executor getSubProjectExecutor() {
        return this;
    }

}
