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


    private static final int SIZE = 128;
    private InputStream is;
    private OutputStream os;
    private boolean finished;
    private boolean closeWhenExhausted;
    private boolean autoflush = false;

    /**
     * Create a new stream pumper.
     *
     * @param is input stream to read data from
     * @param os output stream to write data to.
     * @param closeWhenExhausted if true, the output stream will be closed when
     *        the input is exhausted.
     */
    public StreamPumper(InputStream is, OutputStream os,
                        boolean closeWhenExhausted) {
        this.is = is;
        this.os = os;
        this.closeWhenExhausted = closeWhenExhausted;
    }

    /**
     * Create a new stream pumper.
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
    /*public*/ void setAutoflush(boolean autoflush) {
        this.autoflush = autoflush;
    }

    /**
     * Copies data from the input stream to the output stream.
     *
     * Terminates as soon as the input stream is closed or an error occurs.
     */
    public void run() {
        synchronized (this) {
            finished = false;
        }

        final byte[] buf = new byte[SIZE];

        int length;
        try {
            while ((length = is.read(buf)) > 0 && !finished) {
                os.write(buf, 0, length);
                if (autoflush) {
                    os.flush();
                }
            }
        } catch (Exception e) {
        } finally {
            if (closeWhenExhausted) {
                try {
                    os.close();
                } catch (IOException e) {
                }
            }
            synchronized (this) {
                finished = true;
                notifyAll();
            }
        }
    }

    /**
     * Tells whether the end of the stream has been reached.
     * @return true is the stream has been exhausted.
     **/
    public synchronized boolean isFinished() {
        return finished;
    }

    /**
     * This method blocks until the stream pumper finishes.
     * @see #isFinished()
     **/
    public synchronized void waitFor()
        throws InterruptedException {
        while (!isFinished()) {
            wait();
        }
    }
    
    /**
     * Stop the pumper as soon as possible.
     * Note that it may continue to block on the input stream
     * but it will really stop the thread as soon as it gets EOF
     * or any byte, and it will be marked as finished.
     * @since Ant 1.6.3
     */
    /*public*/ synchronized void stop() {
        finished = true;
        notifyAll();
    }
    
}
