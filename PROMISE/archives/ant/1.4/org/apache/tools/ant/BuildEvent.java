package org.apache.tools.ant;

import java.util.EventObject;

public class BuildEvent extends EventObject {
    private Project project;
    private Target target;
    private Task task;
    private String message;
    private int priority = Project.MSG_VERBOSE;
    private Throwable exception;

    /**
     * Construct a BuildEvent for a project level event
     *
     * @param project the project that emitted the event.
     */
    public BuildEvent(Project project) {
        super(project);
        this.project = project;
        this.target = null;
        this.task = null;
    }
    
    /**
     * Construct a BuildEvent for a target level event
     *
     * @param target the target that emitted the event.
     */
    public BuildEvent(Target target) {
        super(target);
        this.project = target.getProject();
        this.target = target;
        this.task = null;
    }
    
    /**
     * Construct a BuildEvent for a task level event
     *
     * @param task the task that emitted the event.
     */
    public BuildEvent(Task task) {
        super(task);
        this.project = task.getProject();
        this.target = task.getOwningTarget();
        this.task = task;
    }

    public void setMessage(String message, int priority) {
        this.message = message;
        this.priority = priority;
    }
    
    public void setException(Throwable exception) {
        this.exception = exception;
    }

    /**
     *  Returns the project that fired this event.
     */
    public Project getProject() {
        return project;
    }

    /**
     *  Returns the target that fired this event.
     */
    public Target getTarget() {
        
        return target;
    }

    /**
     *  Returns the task that fired this event.
     */
    public Task getTask() {
        return task;
    }

    /**
     *  Returns the logging message. This field will only be set
     *  for "messageLogged" events.
     *
     *  @see BuildListener#messageLogged(BuildEvent)
     */
    public String getMessage() {
        return message;
    }

    /**
     *  Returns the priority of the logging message. This field will only
     *  be set for "messageLogged" events.
     *
     *  @see BuildListener#messageLogged(BuildEvent)
     */
    public int getPriority(){
        return priority;
    }

    /**
     *  Returns the exception that was thrown, if any. This field will only
     *  be set for "taskFinished", "targetFinished", and "buildFinished" events.
     *
     *  @see BuildListener#taskFinished(BuildEvent)
     *  @see BuildListener#targetFinished(BuildEvent)
     *  @see BuildListener#buildFinished(BuildEvent)
     */
    public Throwable getException() {
        return exception;
    }
}
