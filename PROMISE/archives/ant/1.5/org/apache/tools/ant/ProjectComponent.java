package org.apache.tools.ant;

/**
 * Base class for components of a project, including tasks and data types.
 * Provides common facilities.
 *
 * @author Conor MacNeill
 */

public abstract class ProjectComponent {

    /** Project object of this component. */
    protected Project project = null;

    /** Sole constructor. */
    public ProjectComponent() {
    }

    /**
     * Sets the project object of this component. This method is used by
     * Project when a component is added to it so that the component has
     * access to the functions of the project. It should not be used
     * for any other purpose.
     *
     * @param project Project in whose scope this component belongs.
     *                Must not be <code>null</code>.
     */
    public void setProject(Project project) {
        this.project = project;
    }

    /**
     * Returns the project to which this component belongs.
     *
     * @return the components's project.
     */
    public Project getProject() {
        return project;
    }
    
    /**
     * Logs a message with the default (INFO) priority.
     *
     * @param msg The message to be logged. Should not be <code>null</code>.
     */
    public void log(String msg) {
        log(msg, Project.MSG_INFO);
    }

    /**
     * Logs a mesage with the given priority.
     *
     * @param msg The message to be logged. Should not be <code>null</code>.
     * @param msgLevel the message priority at which this message is 
     *                 to be logged.
     */
    public void log(String msg, int msgLevel) {
        if (project != null) {
            project.log(msg, msgLevel);
        }
    }
}
