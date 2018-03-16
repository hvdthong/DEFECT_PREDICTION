package org.apache.tools.ant.taskdefs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Copies all data from an input stream to an output stream.
 *
 * @author thomas.haas@softwired-inc.com
 * @since Ant 1.2
 */
public class StreamPumper implements Runnable {


    private static final int SLEEP = 5;
    private static final int SIZE = 128;
    private InputStream is;
    private OutputStream os;
    private boolean finished;

    /**
     * Create a new stream pumper.
     *
     * @param is input stream to read data from
     * @param os output stream to write data to.
     */
    public StreamPumper(InputStream is, OutputStream os) {
        this.is = is;
        this.os = os;
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
            while ((length = is.read(buf)) > 0) {
                os.write(buf, 0, length);
                try {
                    Thread.sleep(SLEEP);
                } catch (InterruptedException e) {}
            }
        } catch (Exception e) {
        } finally {
            synchronized (this) {
                finished = true;
                notify();
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
}
