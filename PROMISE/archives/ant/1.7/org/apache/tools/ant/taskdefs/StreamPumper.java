package org.apache.tools.ant.taskdefs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Copies all data from an input stream to an output stream.
 *
 * @since Ant 1.2
 */
public class StreamPumper implements Runnable {

    private static final int SMALL_BUFFER_SIZE = 128;

    private InputStream is;
    private OutputStream os;
    private volatile boolean finish;
    private volatile boolean finished;
    private boolean closeWhenExhausted;
    private boolean autoflush = false;
    private Exception exception = null;
    private int bufferSize = SMALL_BUFFER_SIZE;
    private boolean started = false;

    /**
     * Create a new StreamPumper.
     *
     * @param is input stream to read data from
     * @param os output stream to write data to.
     * @param closeWhenExhausted if true, the output stream will be closed when
     *        the input is exhausted.
     */
    public StreamPumper(InputStream is, OutputStream os, boolean closeWhenExhausted) {
        this.is = is;
        this.os = os;
        this.closeWhenExhausted = closeWhenExhausted;
    }

    /**
     * Create a new StreamPumper.
     *
     * @param is input stream to read data from
     * @param os output stream to write data to.
     */
    public StreamPumper(InputStream is, OutputStream os) {
        this(is, os, false);
    }

    /**
     * Set whether data should be flushed through to the output stream.
     * @param autoflush if true, push through data; if false, let it be buffered
     * @since Ant 1.6.3
     */
    /*package*/ void setAutoflush(boolean autoflush) {
        this.autoflush = autoflush;
    }

    /**
     * Copies data from the input stream to the output stream.
     *
     * Terminates as soon as the input stream is closed or an error occurs.
     */
    public void run() {
        synchronized (this) {
            started = true;
        }
        finished = false;
        finish = false;

        final byte[] buf = new byte[bufferSize];

        int length;
        try {
            while ((length = is.read(buf)) > 0 && !finish) {
                os.write(buf, 0, length);
                if (autoflush) {
                    os.flush();
                }
            }
            os.flush();
        } catch (Exception e) {
            synchronized (this) {
                exception = e;
            }
        } finally {
            if (closeWhenExhausted) {
                try {
                    os.close();
                } catch (IOException e) {
                }
            }
            finished = true;
            synchronized (this) {
                notifyAll();
            }
        }
    }

    /**
     * Tells whether the end of the stream has been reached.
     * @return true is the stream has been exhausted.
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * This method blocks until the StreamPumper finishes.
     * @throws InterruptedException if interrupted.
     * @see #isFinished()
     */
    public synchronized void waitFor() throws InterruptedException {
        while (!isFinished()) {
            wait();
        }
    }

    /**
     * Set the size in bytes of the read buffer.
     * @param bufferSize the buffer size to use.
     * @throws IllegalStateException if the StreamPumper is already running.
     */
    public synchronized void setBufferSize(int bufferSize) {
        if (started) {
            throw new IllegalStateException("Cannot set buffer size on a running StreamPumper");
        }
        this.bufferSize = bufferSize;
    }

    /**
     * Get the size in bytes of the read buffer.
     * @return the int size of the read buffer.
     */
    public synchronized int getBufferSize() {
        return bufferSize;
    }

    /**
     * Get the exception encountered, if any.
     * @return the Exception encountered.
     */
    public synchronized Exception getException() {
        return exception;
    }

    /**
     * Stop the pumper as soon as possible.
     * Note that it may continue to block on the input stream
     * but it will really stop the thread as soon as it gets EOF
     * or any byte, and it will be marked as finished.
     * @since Ant 1.6.3
     */
    /*package*/ synchronized void stop() {
        finish = true;
        notifyAll();
    }
}
