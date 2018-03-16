package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

import java.io.OutputStream;
import java.io.InputStream;
import java.io.IOException;

/**
 * Logs standard output and error of a subprocess to the log system of ant.
 *
 * @author thomas.haas@softwired-inc.com
 */
public class LogStreamHandler extends PumpStreamHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param task the task for whom to log
     * @param outlevel the loglevel used to log standard output
     * @param errlevel the loglevel used to log standard error
     */
    public LogStreamHandler(Task task, int outlevel, int errlevel) {
        super(new LogOutputStream(task, outlevel),
              new LogOutputStream(task, errlevel));
    }

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
