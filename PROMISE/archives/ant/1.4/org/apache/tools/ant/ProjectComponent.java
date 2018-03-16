package org.apache.tools.ant;

/**
 * Base class for components of a project, including tasks and data types. Provides
 * common facilities.
 *
 * @author <a href="mailto:conor@apache.org">Conor MacNeill</a>
 */

public abstract class ProjectComponent {

    protected Project project = null;

    /**
     * Sets the project object of this component. This method is used by
     * project when a component is added to it so that the component has
     * access to the functions of the project. It should not be used
     * for any other purpose.
     *
     * @param project Project in whose scope this component belongs.
     */
    public void setProject(Project project) {
        this.project = project;
    }

    /**
     * Get the Project to which this component belongs
     *
     * @return the components's project.
     */
    public Project getProject() {
        return project;
    }
    
    /**
     * Log a message with the default (INFO) priority.
     *
     * @param the message to be logged.
     */
    public void log(String msg) {
        log(msg, Project.MSG_INFO);
    }

    /**
     * Log a mesage with the give priority.
     *
     * @param the message to be logged.
     * @param msgLevel the message priority at which this message is to be logged.
     */
    public void log(String msg, int msgLevel) {
        if (project != null) {
            project.log(msg, msgLevel);
        }
    }
}

