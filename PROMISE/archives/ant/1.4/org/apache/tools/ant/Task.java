package org.apache.tools.ant;

/**
 * Base class for all tasks.
 *
 * <p>Use {@link Project#createTask Project.createTask} to create a new Task.
 */

public abstract class Task extends ProjectComponent {

    protected Target target = null;
    protected String description=null;
    protected Location location = Location.UNKNOWN_LOCATION;
    protected String taskName = null;
    protected String taskType = null;
    protected RuntimeConfigurable wrapper;

    /**
     * Sets the target object of this task.
     *
     * @param target Target in whose scope this task belongs.
     */
    public void setOwningTarget(Target target) {
        this.target = target;
    }

    /**
     * Get the Target to which this task belongs
     *
     * @return the task's target.
     */
    public Target getOwningTarget() {
        return target;
    }
    
    /**
     * Set the name to use in logging messages.
     *
     * @param name the name to use in logging messages.
     */
    public void setTaskName(String name) {
        this.taskName = name;
    }

    /**
     * Get the name to use in logging messages.
     *
     * @return the name to use in logging messages.
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * Set the name with which the task has been invoked.
     *
     * @param type the name the task has been invoked as.
     */
    void setTaskType(String type) {
        this.taskType = type;
    }

    /** Sets a description of the current action. It will be usefull in commenting
     *  what we are doing.
     */
    public void setDescription( String desc ) {
        description=desc;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Called by the project to let the task initialize properly. 
     *
     * @throws BuildException if someting goes wrong with the build
     */
    public void init() throws BuildException {}

    /**
     * Called by the project to let the task do it's work. This method may be 
     * called more than once, if the task is invoked more than once. For example, 
     * if target1 and target2 both depend on target3, then running 
     * "ant target1 target2" will run all tasks in target3 twice.
     *
     * @throws BuildException if someting goes wrong with the build
     */
    public void execute() throws BuildException {}

    /**
     * Returns the file location where this task was defined.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the file location where this task was defined.
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Returns the wrapper class for runtime configuration.
     */
    public RuntimeConfigurable getRuntimeConfigurableWrapper() {
        if (wrapper == null) {
            wrapper = new RuntimeConfigurable(this, getTaskName());
        }
        return wrapper;
    }

    protected void setRuntimeConfigurableWrapper(RuntimeConfigurable wrapper) {
        this.wrapper = wrapper;
    }

    /**
     * Configure this task - if it hasn't been done already.
     */
    public void maybeConfigure() throws BuildException {
        if (wrapper != null) {
            wrapper.maybeConfigure(project);
        }
    }

    protected void handleOutput(String line) {
        log(line, Project.MSG_INFO);
    }
    
    protected void handleErrorOutput(String line) {
        log(line, Project.MSG_ERR);
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
        project.log(this, msg, msgLevel);   
    }   
    
    /**
     * Perform this task
     */
    public final void perform() {
        try {
            project.fireTaskStarted(this);
            maybeConfigure();
            execute();
            project.fireTaskFinished(this, null);
        }
        catch(RuntimeException exc) {
            if (exc instanceof BuildException) {
                BuildException be = (BuildException) exc;
                if (be.getLocation() == Location.UNKNOWN_LOCATION) {
                    be.setLocation(getLocation());
                }
            }
            project.fireTaskFinished(this, exc);
            throw exc;
        }
    }
}

