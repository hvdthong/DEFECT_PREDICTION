package org.apache.tools.ant;

import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;

/**
 * Logs content written by a thread and forwards the buffers onto the
 * project object which will forward the content to the appropriate
 * task.
 *
 * @since 1.4
 * @author Conor MacNeill
 */
public class DemuxOutputStream extends OutputStream {

    /**
     * A data class to store information about a buffer. Such information
     * is stored on a per-thread basis.
     */
    private static class BufferInfo {
        /**
         * The per-thread output stream.
         */
        private ByteArrayOutputStream buffer;
        
        /** 
         * Whether or not the next line-terminator should be skipped in terms
         * of processing the buffer. Used to avoid \r\n invoking
         * processBuffer twice.
         */
         private boolean skip = false;
    }
    
    /** Maximum buffer size. */
    private static final int MAX_SIZE = 1024;
    
    /** Mapping from thread to buffer (Thread to BufferInfo). */
    private Hashtable buffers = new Hashtable();

    /**
     * The project to send output to.
     */
    private Project project;

    /**
     * Whether or not this stream represents an error stream.
     */
    private boolean isErrorStream;
    
    /**
     * Creates a new instance of this class.
     *
     * @param project The project instance for which output is being 
     *                demultiplexed. Must not be <code>null</code>.
     * @param isErrorStream <code>true</code> if this is the error string, 
     *                      otherwise a normal output stream. This is 
     *                      passed to the project so it knows
     *                      which stream it is receiving.
     */
    public DemuxOutputStream(Project project, boolean isErrorStream) {
        this.project = project;
        this.isErrorStream = isErrorStream;
    }

    /**
     * Returns the buffer associated with the current thread.
     * 
     * @return a BufferInfo for the current thread to write data to
     */
    private BufferInfo getBufferInfo() {
        Thread current = Thread.currentThread();
        BufferInfo bufferInfo = (BufferInfo) buffers.get(current);
        if (bufferInfo == null) {
            bufferInfo = new BufferInfo();
            bufferInfo.buffer = new ByteArrayOutputStream();
            bufferInfo.skip = false;
            buffers.put(current, bufferInfo);
        }
        return bufferInfo;
    }

    /**
     * Resets the buffer for the current thread.
     */
    private void resetBufferInfo() {    
        Thread current = Thread.currentThread();
        BufferInfo bufferInfo = (BufferInfo) buffers.get(current);
        try {
            bufferInfo.buffer.close();
        } catch (IOException e) {
        }
        bufferInfo.buffer = new ByteArrayOutputStream();
        bufferInfo.skip = false;
    }
    
    /**
     * Removes the buffer for the current thread.
     */
    private void removeBuffer() {    
        Thread current = Thread.currentThread();
        buffers.remove (current);
    }

    /**
     * Writes the data to the buffer and flushes the buffer if a line
     * separator is detected or if the buffer has reached its maximum size.
     *
     * @param cc data to log (byte).
     * @exception IOException if the data cannot be written to the stream
     */
    public void write(int cc) throws IOException {
        final byte c = (byte) cc;

        BufferInfo bufferInfo = getBufferInfo();
        if ((c == '\n') || (c == '\r')) {
            if (!bufferInfo.skip) {
                processBuffer(bufferInfo.buffer);
            }
        } else {
            bufferInfo.buffer.write(cc);
            if (bufferInfo.buffer.size() > MAX_SIZE) {
                processBuffer(bufferInfo.buffer);
            }
        }
        bufferInfo.skip = (c == '\r');
    }

    /**
     * Converts the buffer to a string and sends it to the project.
     *
     * @param buffer the ByteArrayOutputStream used to collect the output
     * until a line separator is seen.
     * 
     * @see Project#demuxOutput(String,boolean)
     */
    protected void processBuffer(ByteArrayOutputStream buffer) {
        String output = buffer.toString();
        project.demuxOutput(output, isErrorStream);
        resetBufferInfo();
    }

    /**
     * Converts the buffer to a string and sends it to the project.
     *
     * @param buffer the ByteArrayOutputStream used to collect the output
     * until a line separator is seen.
     * 
     * @see Project#demuxOutput(String,boolean)
     */
    protected void processFlush(ByteArrayOutputStream buffer) {
        String output = buffer.toString();
        project.demuxFlush(output, isErrorStream);
        resetBufferInfo();
    }

    /**
     * Equivalent to flushing the stream.
     *
     * @exception IOException if there is a problem closing the stream.
     * 
     * @see #flush
     */
    public void close() throws IOException {
        flush();
        removeBuffer();
    }

    /**
     * Writes all remaining data in the buffer associated
     * with the current thread to the project.
     *
     * @exception IOException if there is a problem flushing the stream.
     */
    public void flush() throws IOException {
        BufferInfo bufferInfo = getBufferInfo();
        if (bufferInfo.buffer.size() > 0) {
            processFlush(bufferInfo.buffer);
        }
    }
}
