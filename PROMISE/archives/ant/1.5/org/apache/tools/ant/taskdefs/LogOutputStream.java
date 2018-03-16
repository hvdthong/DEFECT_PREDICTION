package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;


/**
 * Logs each line written to this stream to the log system of ant.
 *
 * Tries to be smart about line separators.<br>
 * TODO: This class can be split to implement other line based processing
 * of data written to the stream.
 *
 * @author thomas.haas@softwired-inc.com
 * @since Ant 1.2
 */
public class LogOutputStream extends OutputStream {

    private ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    private boolean skip = false;

    private Task task;
    private int level = Project.MSG_INFO;

    /**
     * Creates a new instance of this class.
     *
     * @param task the task for whom to log
     * @param level loglevel used to log data written to this stream.
     */
    public LogOutputStream(Task task, int level) {
        this.task = task;
        this.level = level;
    }


    /**
     * Write the data to the buffer and flush the buffer, if a line
     * separator is detected.
     *
     * @param cc data to log (byte).
     */
    public void write(int cc) throws IOException {
        final byte c = (byte) cc;
        if ((c == '\n') || (c == '\r')) {
            if (!skip) {
              processBuffer();
            }
        } else {
          buffer.write(cc);
        }
        skip = (c == '\r');
    }


    /**
     * Converts the buffer to a string and sends it to <code>processLine</code>
     */
    protected void processBuffer() {
        processLine(buffer.toString());
        buffer.reset();
    }

    /**
     * Logs a line to the log system of ant.
     *
     * @param line the line to log.
     */
    protected void processLine(String line) {
        processLine(line, level);
    }

    /**
     * Logs a line to the log system of ant.
     *
     * @param line the line to log.
     */
    protected void processLine(String line, int level) {
        task.log(line, level);
    }


    /**
     * Writes all remaining
     */
    public void close() throws IOException {
        if (buffer.size() > 0) {
          processBuffer();
        }
        super.close();
    }

    public int getMessageLevel() {
        return level;
    }
}
