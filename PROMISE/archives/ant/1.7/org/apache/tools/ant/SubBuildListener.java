package org.apache.tools.ant;

/**
 * Instances of classes that implement this interface can register
 * to be also notified when things happened during a subbuild.
 *
 * <p>A subbuild is a separate project instance created by the
 * <code>&lt;ant&gt;</code> task family.  These project instances will
 * never fire the buildStarted and buildFinished events, but they will
 * fire subBuildStarted/ and subBuildFinished.  The main project
 * instance - the one created by running Ant in the first place - will
 * never invoke one of the methods of this interface.</p>
 *
 * @see BuildEvent
 * @see Project#addBuildListener(BuildListener)
 *
 * @since Ant 1.6.2
 */
public interface SubBuildListener extends BuildListener {

    /**
     * Signals that a subbuild has started. This event
     * is fired before any targets have started.
     *
     * @param event An event with any relevant extra information.
     *              Must not be <code>null</code>.
     */
    void subBuildStarted(BuildEvent event);

    /**
     * Signals that the last target has finished. This event
     * will still be fired if an error occurred during the build.
     *
     * @param event An event with any relevant extra information.
     *              Must not be <code>null</code>.
     *
     * @see BuildEvent#getException()
     */
    void subBuildFinished(BuildEvent event);
}
