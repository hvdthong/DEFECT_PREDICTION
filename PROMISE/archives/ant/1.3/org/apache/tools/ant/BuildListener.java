package org.apache.tools.ant;

import java.util.EventListener;

/**
 *  Classes that implement this interface will be notified when
 *  things happend during a build.
 *
 *  @see BuildEvent
 *  @see Project#addBuildListener(BuildListener)
 */
public interface BuildListener extends EventListener {

    /**
     *  Fired before any targets are started.
     */
    public void buildStarted(BuildEvent event);

    /**
     *  Fired after the last target has finished. This event
     *  will still be thrown if an error occured during the build.
     *
     *  @see BuildEvent#getException()
     */
    public void buildFinished(BuildEvent event);

    /**
     *  Fired when a target is started.
     *
     *  @see BuildEvent#getTarget()
     */
    public void targetStarted(BuildEvent event);

    /**
     *  Fired when a target has finished. This event will
     *  still be thrown if an error occured during the build.
     *
     *  @see BuildEvent#getException()
     */
    public void targetFinished(BuildEvent event);

    /**
     *  Fired when a task is started.
     *
     *  @see BuildEvent#getTask()
     */
    public void taskStarted(BuildEvent event);

    /**
     *  Fired when a task has finished. This event will still
     *  be throw if an error occured during the build.
     *
     *  @see BuildEvent#getException()
     */
    public void taskFinished(BuildEvent event);

    /**
     *  Fired whenever a message is logged.
     *
     *  @see BuildEvent#getMessage()
     *  @see BuildEvent#getPriority()
     */
    public void messageLogged(BuildEvent event);

}
