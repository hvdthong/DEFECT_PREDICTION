package org.apache.tools.ant;


/**
 * Base class for components of a project, including tasks and data types.
 * Provides common facilities.
 *
 */
public abstract class ProjectComponent implements Cloneable {

    /**
     * Project object of this component.
     * @deprecated since 1.6.x.
     *             You should not be directly accessing this variable directly.
     *             You should access project object via the getProject()
     *             or setProject() accessor/mutators.
     */
    protected Project project;

    /**
     * Location within the build file of this task definition.
     * @deprecated since 1.6.x.
     *             You should not be accessing this variable directly.
     *             Please use the {@link #getLocation()} method.
     */
    protected Location location = Location.UNKNOWN_LOCATION;

    /**
     * Description of this component, if any.
     * @deprecated since 1.6.x.
     *             You should not be accessing this variable directly.
     */
    protected String description;

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
     * Returns the file/location where this task was defined.
     *
     * @return the file/location where this task was defined.
     *         Should not return <code>null</code>. Location.UNKNOWN_LOCATION
     *         is used for unknown locations.
     *
     * @see Location#UNKNOWN_LOCATION
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the file/location where this task was defined.
     *
     * @param location The file/location where this task was defined.
     *                 Should not be <code>null</code>--use
     *                 Location.UNKNOWN_LOCATION if the location isn't known.
     *
     * @see Location#UNKNOWN_LOCATION
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Sets a description of the current action. This may be used for logging
     * purposes.
     *
     * @param desc Description of the current action.
     *             May be <code>null</code>, indicating that no description is
     *             available.
     *
     */
    public void setDescription(String desc) {
        description = desc;
    }

    /**
     * Returns the description of the current action.
     *
     * @return the description of the current action, or <code>null</code> if
     *         no description is available.
     */
    public String getDescription() {
        return description;
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
        if (getProject() != null) {
            getProject().log(msg, msgLevel);
        } else {
            if (msgLevel <= Project.MSG_INFO) {
                System.err.println(msg);
            }
        }
    }
    /**
     * @since Ant 1.7
     * @return a shallow copy of this projectcomponent.
     * @throws CloneNotSupportedException does not happen,
     *                                    but is declared to allow subclasses to do so.
     */
    public Object clone() throws CloneNotSupportedException {
        ProjectComponent pc = (ProjectComponent) super.clone();
        pc.setLocation(getLocation());
        pc.setProject(getProject());
        return pc;
    }
}
