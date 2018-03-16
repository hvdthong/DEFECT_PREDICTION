package org.apache.tools.ant.util;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * UUEncoding of an input stream placed into an outputstream.
 * This class is meant to be a drop in replacement for
 * sun.misc.UUEncoder, which was previously used by Ant.
 * The uuencode algorithm code has been copied from the
 * geronimo project.
 **/

public class UUEncoder {
    protected static final int DEFAULT_MODE = 644;
    private static final int MAX_CHARS_PER_LINE = 45;
    private static final int INPUT_BUFFER_SIZE = MAX_CHARS_PER_LINE * 100;
    private OutputStream out;
    private String name;

    /**
     * Constructor specifing a name for the encoded buffer, begin
     * line will be:
     * <pre>
     *   begin 644 [NAME]
     * </pre>
     * @param name the name of the encoded buffer.
     */
    public UUEncoder(String name) {
        this.name = name;
    }

    /**
     * UUEncode bytes from the input stream, and write them as text characters
     * to the output stream. This method will run until it exhausts the
     * input stream.
     * @param is the input stream.
     * @param out the output stream.
     * @throws IOException if there is an error.
     */
    public void encode(InputStream is, OutputStream out)
        throws IOException {
        this.out = out;
        encodeBegin();
        byte[] buffer = new byte[INPUT_BUFFER_SIZE];
        int count;
        while ((count = is.read(buffer, 0, buffer.length)) != -1) {
            int pos = 0;
            while (count > 0) {
                int num = count > MAX_CHARS_PER_LINE
                    ? MAX_CHARS_PER_LINE
                    : count;
                encodeLine(buffer, pos, num, out);
                pos += num;
                count -= num;
            }
        }
        out.flush();
        encodeEnd();
    }

    /**
     * Encode a string to the output.
     */
    private void encodeString(String n) throws IOException {
        PrintStream writer = new PrintStream(out);
        writer.print(n);
        writer.flush();
    }

    private void encodeBegin() throws IOException {
        encodeString("begin " + DEFAULT_MODE + " " + name + "\n");
    }

    private void encodeEnd() throws IOException {
        encodeString(" \nend\n");
    }

    /**
     * Encode a single line of data (less than or equal to 45 characters).
     *
     * @param data   The array of byte data.
     * @param off    The starting offset within the data.
     * @param length Length of the data to encode.
     * @param out    The output stream the encoded data is written to.
     *
     * @exception IOException
     */
    private void encodeLine(
        byte[] data, int offset, int length, OutputStream out)
        throws IOException {
        out.write((byte) ((length & 0x3F) + ' '));
        byte a;
        byte b;
        byte c;

        for (int i = 0; i < length;) {
            b = 1;
            c = 1;
            a = data[offset + i++];
            if (i < length) {
                b = data[offset + i++];
                if (i < length) {
                    c = data[offset + i++];
                }
            }

            byte d1 = (byte) (((a >>> 2) & 0x3F) + ' ');
            byte d2 = (byte) ((((a << 4) & 0x30) | ((b >>> 4) & 0x0F)) + ' ');
            byte d3 = (byte) ((((b << 2) & 0x3C) | ((c >>> 6) & 0x3)) + ' ');
            byte d4 = (byte) ((c & 0x3F) + ' ');

            out.write(d1);
            out.write(d2);
            out.write(d3);
            out.write(d4);
        }

        out.write('\n');
    }
}
