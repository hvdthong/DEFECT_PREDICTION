package org.apache.tools.ant.taskdefs.optional.perforce;

import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * heavily inspired from LogOutputStream
 * this stream class calls back the P4Handler on each line of stdout or stderr read
 */
public class P4OutputStream extends OutputStream {
    private P4Handler handler;
    private ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    private boolean skip = false;

    /**
     * creates a new P4OutputStream for a P4Handler
     * @param handler   the handler which will process the streams
     */
    public P4OutputStream(P4Handler handler) {
        this.handler = handler;
    }

    /**
     * Write the data to the buffer and flush the buffer, if a line
     * separator is detected.
     *
     * @param cc data to log (byte).
     * @throws IOException IOException  if an I/O error occurs. In particular,
     *             an <code>IOException</code> may be thrown if the
     *             output stream has been closed.
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
        handler.process(buffer.toString());
        buffer.reset();
    }

    /**
     * Writes all remaining
     * @throws IOException if an I/O error occurs.
     */
    public void close() throws IOException {
        if (buffer.size() > 0) {
            processBuffer();
        }
        super.close();
    }

}


