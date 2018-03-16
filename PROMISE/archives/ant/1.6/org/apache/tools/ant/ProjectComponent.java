package org.apache.tools.ant;

/**
 * Base class for components of a project, including tasks and data types.
 * Provides common facilities.
 *
 */
public abstract class ProjectComponent {

    /**
     * Project object of this component.
     * @deprecated You should not be directly accessing this variable
     *   directly. You should access project object via the getProject()
     *   or setProject() accessor/mutators.
     */
    protected Project project;

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
     * Logs a message with the given priority.
     *
     * @param msg The message to be logged. Should not be <code>null</code>.
     * @param msgLevel the message priority at which this message is
     *                 to be logged.
     */
    public void log(String msg, int msgLevel) {
        if (project != null) {
            project.log(msg, msgLevel);
        } else {
            if (msgLevel <= Project.MSG_INFO) {
                System.err.println(msg);
            }
        }
    }
}
