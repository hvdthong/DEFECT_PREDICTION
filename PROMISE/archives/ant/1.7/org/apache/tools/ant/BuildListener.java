package org.apache.tools.ant;

import java.util.EventListener;

/**
 * Instances of classes that implement this interface can register
 * to be notified when things happened during a build.
 *
 * @see BuildEvent
 * @see Project#addBuildListener(BuildListener)
 *
 */
public interface BuildListener extends EventListener {

    /**
     * Signals that a build has started. This event
     * is fired before any targets have started.
     *
     * @param event An event with any relevant extra information.
     *              Must not be <code>null</code>.
     */
    void buildStarted(BuildEvent event);

    /**
     * Signals that the last target has finished. This event
     * will still be fired if an error occurred during the build.
     *
     * @param event An event with any relevant extra information.
     *              Must not be <code>null</code>.
     *
     * @see BuildEvent#getException()
     */
    void buildFinished(BuildEvent event);

    /**
     * Signals that a target is starting.
     *
     * @param event An event with any relevant extra information.
     *              Must not be <code>null</code>.
     *
     * @see BuildEvent#getTarget()
     */
    void targetStarted(BuildEvent event);

    /**
     * Signals that a target has finished. This event will
     * still be fired if an error occurred during the build.
     *
     * @param event An event with any relevant extra information.
     *              Must not be <code>null</code>.
     *
     * @see BuildEvent#getException()
     */
    void targetFinished(BuildEvent event);

    /**
     * Signals that a task is starting.
     *
     * @param event An event with any relevant extra information.
     *              Must not be <code>null</code>.
     *
     * @see BuildEvent#getTask()
     */
    void taskStarted(BuildEvent event);

    /**
     * Signals that a task has finished. This event will still
     * be fired if an error occurred during the build.
     *
     * @param event An event with any relevant extra information.
     *              Must not be <code>null</code>.
     *
     * @see BuildEvent#getException()
     */
    void taskFinished(BuildEvent event);

    /**
     * Signals a message logging event.
     *
     * @param event An event with any relevant extra information.
     *              Must not be <code>null</code>.
     *
     * @see BuildEvent#getMessage()
     * @see BuildEvent#getException()
     * @see BuildEvent#getPriority()
     */
    void messageLogged(BuildEvent event);
}
