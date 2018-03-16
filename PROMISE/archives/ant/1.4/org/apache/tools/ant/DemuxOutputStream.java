package org.apache.tools.ant;

import java.io.*;
import java.util.*;


/**
 * Logs content written by a thread and forwards the buffers onto the
 * project object which will forward the content to the appropriate
 * task 
 *
 * @author Conor MacNeill
 */
public class DemuxOutputStream extends OutputStream {

    static private final int MAX_SIZE = 1024;
    
    private Hashtable buffers = new Hashtable();
    private boolean skip = false;
    private Project project;
    private boolean isErrorStream;
    
    /**
     * Creates a new instance of this class.
     *
     * @param task the task for whom to log
     * @param level loglevel used to log data written to this stream.
     */
    public DemuxOutputStream(Project project, boolean isErrorStream) {
        this.project = project;
        this.isErrorStream = isErrorStream;
    }

    private ByteArrayOutputStream getBuffer() {
        Thread current = Thread.currentThread();
        ByteArrayOutputStream buffer = (ByteArrayOutputStream)buffers.get(current);
        if (buffer == null) {
            buffer = new ByteArrayOutputStream();
            buffers.put(current, buffer);
        }
        return buffer;
    }

    private void resetBuffer() {    
        Thread current = Thread.currentThread();
        buffers.remove(current);
    }
    
    /**
     * Write the data to the buffer and flush the buffer, if a line
     * separator is detected.
     *
     * @param cc data to log (byte).
     */
    public void write(int cc) throws IOException {
        final byte c = (byte)cc;
        if ((c == '\n') || (c == '\r')) {
            if (!skip) {
                processBuffer();
            }
        } else {
            ByteArrayOutputStream buffer = getBuffer();
            buffer.write(cc);
            if (buffer.size() > MAX_SIZE) {
                processBuffer();
            }
        }
        skip = (c == '\r');
    }


    /**
     * Converts the buffer to a string and sends it to <code>processLine</code>
     */
    protected void processBuffer() {
        String output = getBuffer().toString();
        project.demuxOutput(output, isErrorStream);
        resetBuffer();
    }

    /**
     * Writes all remaining
     */
    public void close() throws IOException {
        flush();
    }

    /**
     * Writes all remaining
     */
    public void flush() throws IOException {
        if (getBuffer().size() > 0) {
            processBuffer();
        }
    }
}
