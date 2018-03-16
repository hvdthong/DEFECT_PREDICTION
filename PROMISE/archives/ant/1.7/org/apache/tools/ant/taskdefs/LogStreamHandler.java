package org.apache.tools.ant.taskdefs;

import java.io.IOException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.Task;

/**
 * Logs standard output and error of a subprocess to the log system of ant.
 *
 * @since Ant 1.2
 */
public class LogStreamHandler extends PumpStreamHandler {

    /**
     * Creates log stream handler
     *
     * @param task the task for whom to log
     * @param outlevel the loglevel used to log standard output
     * @param errlevel the loglevel used to log standard error
     */
    public LogStreamHandler(Task task, int outlevel, int errlevel) {
        this((ProjectComponent) task, outlevel, errlevel);
    }

    /**
     * Creates log stream handler
     *
     * @param pc the project component for whom to log
     * @param outlevel the loglevel used to log standard output
     * @param errlevel the loglevel used to log standard error
     */
    public LogStreamHandler(ProjectComponent pc, int outlevel, int errlevel) {
        super(new LogOutputStream(pc, outlevel),
              new LogOutputStream(pc, errlevel));
    }

    /**
     * Stop the log stream handler.
     */
    public void stop() {
        super.stop();
        try {
            getErr().close();
            getOut().close();
        } catch (IOException e) {
            throw new BuildException(e);
        }
    }
}
