package org.apache.tools.ant.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Invokes {@link #processLine processLine} whenever a full line has
 * been written to this stream.
 *
 * <p>Tries to be smart about line separators.</p>
 */
public abstract class LineOrientedOutputStream extends OutputStream {

    /** Initial buffer size. */
    private static final int INTIAL_SIZE = 132;

    /** Carriage return */
    private static final int CR = 0x0d;

    /** Linefeed */
    private static final int LF = 0x0a;

    private ByteArrayOutputStream buffer
        = new ByteArrayOutputStream(INTIAL_SIZE);
    private boolean skip = false;

    /**
     * Write the data to the buffer and flush the buffer, if a line
     * separator is detected.
     *
     * @param cc data to log (byte).
     * @throws IOException if there is an error.
     */
    public final void write(int cc) throws IOException {
        final byte c = (byte) cc;
        if ((c == LF) || (c == CR)) {
            if (!skip) {
              processBuffer();
            }
        } else {
            buffer.write(cc);
        }
        skip = (c == CR);
    }

    /**
     * Flush this log stream
     * @throws IOException if there is an error.
     */
    public final void flush() throws IOException {
        if (buffer.size() > 0) {
            processBuffer();
        }
    }

    /**
     * Converts the buffer to a string and sends it to
     * <code>processLine</code>
     * @throws IOException if there is an error.
     */
    protected void processBuffer() throws IOException {
        try {
            processLine(buffer.toString());
        } finally {
            buffer.reset();
        }
    }

    /**
     * Processes a line.
     *
     * @param line the line to log.
     * @throws IOException if there is an error.
     */
    protected abstract void processLine(String line) throws IOException;

    /**
     * Writes all remaining
     * @throws IOException if there is an error.
     */
    public final void close() throws IOException {
        if (buffer.size() > 0) {
            processBuffer();
        }
        super.close();
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
    public final void write(byte[] b, int off, int len) throws IOException {
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
