package org.apache.ivy.ant;

import org.apache.ivy.Ivy;
import org.apache.ivy.util.AbstractMessageLogger;
import org.apache.ivy.util.Checks;
import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildListener;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.Task;

/**
 * Implementation of the simple message facility for ant.
 */
public class AntMessageLogger extends AbstractMessageLogger {
    private static final int PROGRESS_LOG_PERIOD = 1500;

    /**
     * Creates and register an {@link AntMessageLogger} for the given {@link Task}, with the given
     * {@link Ivy} instance.
     * <p>
     * The created instance will automatically be unregistered from the Ivy instance when the task
     * finishes.
     * </p>
     * 
     * @param task
     *            the task the logger should use for logging
     * @param ivy
     *            the ivy instance on which the logger should be registered
     */
    public static void register(Task task, final Ivy ivy) {
        AntMessageLogger logger = new AntMessageLogger(task);
        ivy.getLoggerEngine().pushLogger(logger);
        task.getProject().addBuildListener(new BuildListener() {
            private int stackDepth = 0;

            public void buildFinished(BuildEvent event) {
            }

            public void buildStarted(BuildEvent event) {
            }

            public void targetStarted(BuildEvent event) {
            }

            public void targetFinished(BuildEvent event) {
            }

            public void taskStarted(BuildEvent event) {
                stackDepth++;
            }

            public void taskFinished(BuildEvent event) {
                stackDepth--;
                if (stackDepth == 0) {
                    ivy.getLoggerEngine().popLogger();
                    event.getProject().removeBuildListener(this);
                }
            }

            public void messageLogged(BuildEvent event) {
            }
        });
        
    }

    private ProjectComponent projectComponent;

    private long lastProgressFlush = 0;

    private StringBuffer buf = new StringBuffer();

    /**
     * Constructs a new AntMEssageImpl instance.
     * 
     * @param antProjectComponent
     *            the ant project component this message implementation should use for logging. Must
     *            not be <code>null</code>.
     */
    protected AntMessageLogger(ProjectComponent antProjectComponent) {
        Checks.checkNotNull(antProjectComponent, "antProjectComponent");
        projectComponent = antProjectComponent;
    }

    public void log(String msg, int level) {
        projectComponent.log(msg, level);
    }

    public void rawlog(String msg, int level) {
        projectComponent.getProject().log(msg, level);
    }

    public void doProgress() {
        buf.append(".");
        if (lastProgressFlush == 0) {
            lastProgressFlush = System.currentTimeMillis();
        }
        if (System.currentTimeMillis() - lastProgressFlush > PROGRESS_LOG_PERIOD) {
            projectComponent.log(buf.toString());
            buf.setLength(0);
            lastProgressFlush = System.currentTimeMillis();
        }
    }

    public void doEndProgress(String msg) {
        projectComponent.log(buf + msg);
        buf.setLength(0);
        lastProgressFlush = 0;
    }
    
    public String toString() {
        return "AntMessageLogger:" + projectComponent;
    }
}
