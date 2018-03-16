package org.apache.tools.ant.taskdefs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Copies all data from an input stream to an output stream.
 *
 * @author thomas.haas@softwired-inc.com
 */
public class StreamPumper implements Runnable {


    private final static int SLEEP = 5;
    private final static int SIZE = 128;
    private InputStream is;
    private OutputStream os;


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
        final byte[] buf = new byte[SIZE];

        int length;
        try {
            while ((length = is.read(buf)) > 0) {
                os.write(buf, 0, length);
                try {
                    Thread.sleep(SLEEP);
                } catch (InterruptedException e) {}
            }
        } catch(IOException e) {}
    }
}
