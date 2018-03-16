package org.apache.tools.ant;

import java.util.EventObject;

/**
 * Class representing an event occurring during a build. An
 * event is built by specifying either a project, a task or a target.
 * A project level event will only have a project reference;
 * a target level event will have project and target references;
 * a task level event will have project, target and task references.
 *
 * @author Matt Foemmel
 */
public class BuildEvent extends EventObject {
    
    /** Project which emitted the event. */
    private Project project;
    /** Target which emitted the event, if specified. */
    private Target target;
    /** Task which emitted the event, if specified. */
    private Task task;
    /** 
     * Message associated with the event. This is only used for
     * "messageLogged" events.
     */
    private String message;
    /**
     * The priority of the message, for "messageLogged" events.
     */
    private int priority = Project.MSG_VERBOSE;
    /**
     * The exception associated with this event, if any.
     * This is only used for "taskFinished", "targetFinished", 
     * and "buildFinished" events.
     */
    private Throwable exception;

    /**
     * Construct a BuildEvent for a project level event.
     *
     * @param project the project that emitted the event.
     *                Should not be <code>null</code>.
     */
    public BuildEvent(Project project) {
        super(project);
        this.project = project;
        this.target = null;
        this.task = null;
    }
    
    /**
     * Construct a BuildEvent for a target level event.
     * The project associated with the event is derived
     * from the given target.
     *
     * @param target the target that emitted the event.
     *               Must not be <code>null</code>.
     */
    public BuildEvent(Target target) {
        super(target);
        this.project = target.getProject();
        this.target = target;
        this.task = null;
    }
    
    /**
     * Construct a BuildEvent for a task level event.
     * The project and target associated with the event 
     * are derived from the given task.
     *
     * @param task the task that emitted the event.
     *             Must not be <code>null</code>.
     */
    public BuildEvent(Task task) {
        super(task);
        this.project = task.getProject();
        this.target = task.getOwningTarget();
        this.task = task;
    }

    /**
     * Sets the message and priority associated with this event.
     * This is used for "messageLogged" events.
     * 
     * @param message the message to be associated with this event.
     *                Should not be <code>null</code>.
     * @param priority the priority to be associated with this event,
     *                 as defined in the {@link Project Project} class.
     *
     * @see BuildListener#messageLogged(BuildEvent)
     */
    public void setMessage(String message, int priority) {
        this.message = message;
        this.priority = priority;
    }
    
    /**
     * Sets the exception associated with this event. This is used 
     * for "taskFinished", "targetFinished", and "buildFinished" 
     * events.
     * 
     * @param exception The exception to be associated with this event.
     *                  May be <code>null</code>.
     *
     * @see BuildListener#taskFinished(BuildEvent)
     * @see BuildListener#targetFinished(BuildEvent)
     * @see BuildListener#buildFinished(BuildEvent)
     */
    public void setException(Throwable exception) {
        this.exception = exception;
    }

    /**
     * Returns the project that fired this event.
     * 
     * @return the project that fired this event
     */
    public Project getProject() {
        return project;
    }

    /**
     * Returns the target that fired this event.
     * 
     * @return the project that fired this event, or <code>null</code>
     *          if this event is a project level event.
     */
    public Target getTarget() {
        
        return target;
    }

    /**
     * Returns the task that fired this event.
     * 
     * @return the task that fired this event, or <code>null</code>
     *         if this event is a project or target level event.
     */
    public Task getTask() {
        return task;
    }

    /**
     * Returns the logging message. This field will only be set
     * for "messageLogged" events.
     *
     * @return the message associated with this event, or <code>null</code>
     *         if no message has been set.
     * 
     * @see BuildListener#messageLogged(BuildEvent)
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the priority of the logging message. This field will only
     * be set for "messageLogged" events. The meaning of this priority
     * is as specified by the constants in the {@link Project Project} class.
     * 
     * @return the priority associated with this event.
     *
     * @see BuildListener#messageLogged(BuildEvent)
     */
    public int getPriority(){
        return priority;
    }

    /**
     * Returns the exception that was thrown, if any. This field will only
     * be set for "taskFinished", "targetFinished", and "buildFinished"
     * events.
     * 
     * @return the exception associated with this exception, or 
     *         <code>null</code> if no exception has been set.
     *
     * @see BuildListener#taskFinished(BuildEvent)
     * @see BuildListener#targetFinished(BuildEvent)
     * @see BuildListener#buildFinished(BuildEvent)
     */
    public Throwable getException() {
        return exception;
    }
}
