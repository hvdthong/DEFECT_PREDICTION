package org.apache.tools.ant.listener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogConfigurationException;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildListener;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;

/**
 * Jakarta Commons Logging listener.
 * Note: do not use the SimpleLog as your logger implementation as it
 * causes an infinite loop since it writes to System.err, which Ant traps
 * and reroutes to the logger/listener layer.
 *
 * @author Erik Hatcher
 * @since Ant 1.5
 */
public class CommonsLoggingListener implements BuildListener {

    /** Indicates if the listener was initialized. */
    private boolean initialized = false;

    private LogFactory logFactory;

    /**
     * Construct the listener and make sure that a LogFactory
     * can be obtained.
     */
    public CommonsLoggingListener() {
        try {
            logFactory = LogFactory.getFactory();
        } catch (LogConfigurationException e) {
            e.printStackTrace(System.err);
            return;
        }

        initialized = true;
    }

    /**
     * @see BuildListener#buildStarted
     */
    public void buildStarted(BuildEvent event) {
        if (initialized) {
            Log log = logFactory.getInstance(Project.class);
            log.info("Build started.");
        }
    }

    /**
     * @see BuildListener#buildFinished
     */
    public void buildFinished(BuildEvent event) {
        if (initialized) {
            Log log = logFactory.getInstance(Project.class);
            if (event.getException() == null) {
                log.info("Build finished.");
            } else {
                log.error("Build finished with error.", event.getException());
            }
        }
    }

    /**
     * @see BuildListener#targetStarted
     */
    public void targetStarted(BuildEvent event) {
        if (initialized) {
            Log log = logFactory.getInstance(Target.class);
            log.info("Target \"" + event.getTarget().getName() + "\" started.");
        }
    }

    /**
     * @see BuildListener#targetFinished
     */
    public void targetFinished(BuildEvent event) {
        if (initialized) {
            String targetName = event.getTarget().getName();
            Log log = logFactory.getInstance(Target.class);
            if (event.getException() == null) {
                log.info("Target \"" + targetName + "\" finished.");
            } else {
                log.error("Target \"" + targetName
                        + "\" finished with error.", event.getException());
            }
        }
    }

    /**
     * @see BuildListener#taskStarted
     */
    public void taskStarted(BuildEvent event) {
        if (initialized) {
            Task task = event.getTask();
            Log log = logFactory.getInstance(task.getClass().getName());
            log.info("Task \"" + task.getTaskName() + "\" started.");
        }
    }

    /**
     * @see BuildListener#taskFinished
     */
    public void taskFinished(BuildEvent event) {
        if (initialized) {
            Task task = event.getTask();
            Log log = logFactory.getInstance(task.getClass().getName());
            if (event.getException() == null) {
                log.info("Task \"" + task.getTaskName() + "\" finished.");
            } else {
                log.error("Task \"" + task.getTaskName()
                        + "\" finished with error.", event.getException());
            }
        }
    }

    /**
     * @see BuildListener#messageLogged
     */
    public void messageLogged(BuildEvent event) {
        if (initialized) {
            Object categoryObject = event.getTask();
            if (categoryObject == null) {
                categoryObject = event.getTarget();
                if (categoryObject == null) {
                    categoryObject = event.getProject();
                }
            }

            Log log = logFactory.getInstance(categoryObject.getClass().getName());
            switch (event.getPriority()) {
                case Project.MSG_ERR:
                    log.error(event.getMessage());
                    break;
                case Project.MSG_WARN:
                    log.warn(event.getMessage());
                    break;
                case Project.MSG_INFO:
                    log.info(event.getMessage());
                    break;
                case Project.MSG_VERBOSE:
                    log.debug(event.getMessage());
                    break;
                case Project.MSG_DEBUG:
                    log.debug(event.getMessage());
                    break;
                default:
                    log.error(event.getMessage());
                    break;
            }
        }
    }
}
