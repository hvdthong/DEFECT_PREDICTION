package org.apache.tools.ant;

/**
 * Target executor abstraction.
 * @since Ant 1.6.3
 */
public interface Executor {

    /**
     * Execute the specified Targets for the specified Project.
     * @param project       the Ant Project.
     * @param targetNames   String[] of Target names.
     * @throws BuildException on error
     */
    void executeTargets(Project project, String[] targetNames)
        throws BuildException;

    /**
     * Get the appropriate subproject Executor instance.
     * @return an Executor instance.
     */
    Executor getSubProjectExecutor();

}
