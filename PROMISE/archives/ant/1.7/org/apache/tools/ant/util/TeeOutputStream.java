package org.apache.tools.ant.util;

import java.io.OutputStream;
import java.io.IOException;

/**
 * A simple T-piece to replicate an output stream into two separate streams
 *
 */
public class TeeOutputStream extends OutputStream {
    private OutputStream left;
    private OutputStream right;

    /**
     * Constructor for TeeOutputStream.
     * @param left one of the output streams.
     * @param right the other output stream.
     */
    public TeeOutputStream(OutputStream left, OutputStream right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Close both output streams.
     * @throws IOException on error.
     */
    public void close() throws IOException {
        try {
            left.close();
        } finally {
            right.close();
        }
    }

    /**
     * Flush both output streams.
     * @throws IOException on error
     */
    public void flush() throws IOException {
        left.flush();
        right.flush();
    }

    /**
     * Write a byte array to both output streams.
     * @param b an array of bytes.
     * @throws IOException on error.
     */
    public void write(byte[] b) throws IOException {
        left.write(b);
        right.write(b);
    }

    /**
     * Write a byte array to both output streams.
     * @param b     the data.
     * @param off   the start offset in the data.
     * @param len   the number of bytes to write.
     * @throws IOException on error.
     */
    public void write(byte[] b, int off, int len) throws IOException {
        left.write(b, off, len);
        right.write(b, off, len);
    }

    /**
     * Write a byte to both output streams.
     * @param b the byte to write.
     * @throws IOException on error.
     */
    public void write(int b) throws IOException {
        left.write(b);
        right.write(b);
    }
}

