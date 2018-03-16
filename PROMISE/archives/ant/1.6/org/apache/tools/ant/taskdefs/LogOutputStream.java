package org.apache.tools.ant.taskdefs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.Task;


/**
 * Logs each line written to this stream to the log system of ant.
 *
 * Tries to be smart about line separators.<br>
 * TODO: This class can be split to implement other line based processing
 * of data written to the stream.
 *
 * @since Ant 1.2
 */
public class LogOutputStream extends OutputStream {

    /** Initial buffer size. */
    private static final int INTIAL_SIZE = 132;

    /** Carriage return */
    private static final int CR = 0x0d;

    /** Linefeed */
    private static final int LF = 0x0a;

    private ByteArrayOutputStream buffer
        = new ByteArrayOutputStream(INTIAL_SIZE);
    private boolean skip = false;

    private ProjectComponent pc;
    private int level = Project.MSG_INFO;

    /**
     * Creates a new instance of this class.
     *
     * @param task the task for whom to log
     * @param level loglevel used to log data written to this stream.
     */
    public LogOutputStream(Task task, int level) {
        this((ProjectComponent) task, level);
    }

    /**
     * Creates a new instance of this class.
     *
     * @param task the task for whom to log
     * @param level loglevel used to log data written to this stream.
     * @since Ant 1.6.3
     */
    public LogOutputStream(ProjectComponent pc, int level) {
        this.pc = pc;
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
     * Flush this log stream
     */
    public void flush() {
        if (buffer.size() > 0) {
            processBuffer();
        }
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
        pc.log(line, level);
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

    /**
     * Write a block of characters to the output stream
     *
     * @param b the array containing the data
     * @param off the offset into the array where data starts
     * @param len the length of block
     *
     * @throws IOException if the data cannot be written into the stream.
     */
    public void write(byte[] b, int off, int len) throws IOException {
        int offset = off;
        int blockStartOffset = offset;
        int remaining = len;
        while (remaining > 0) {
            while (remaining > 0 && b[offset] != LF && b[offset] != CR) {
                offset++;
                remaining--;
            }
            int blockLength = offset - blockStartOffset;
            if (blockLength > 0) {
                buffer.write(b, blockStartOffset, blockLength);
            }
            while (remaining > 0 && (b[offset] == LF || b[offset] == CR)) {
                write(b[offset]);
                offset++;
                remaining--;
            }
            blockStartOffset = offset;
        }
    }

}
