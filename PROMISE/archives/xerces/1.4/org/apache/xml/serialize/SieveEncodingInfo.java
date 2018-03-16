package org.apache.xml.serialize;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * This class represents an encoding.
 *
 * @version $Id: SieveEncodingInfo.java 316729 2000-12-14 19:21:56Z lehors $
 */
public class SieveEncodingInfo extends EncodingInfo {

    BAOutputStream checkerStream = null;
    Writer checkerWriter = null;
    String dangerChars = null;

    /**
     * Creates new <code>SeiveEncodingInfo</code> instance.
     *
     * @param dangers A sorted characters that are always printed as character references.
     */
    public SieveEncodingInfo(String mimeName, String javaName,
                             int lastPrintable, String dangers) {
        super(mimeName, javaName, lastPrintable);
        this.dangerChars = dangers;
    }

    /**
     * Creates new <code>SeiveEncodingInfo</code> instance.
     */
    public SieveEncodingInfo(String mimeName, int lastPrintable) {
        this(mimeName, mimeName, lastPrintable, null);
    }

    /**
     * Checks whether the specified character is printable or not.
     *
     * @param ch a code point (0-0x10ffff)
     */
    public boolean isPrintable(int ch) {
        if (this.dangerChars != null && ch <= 0xffff) {
            /**
             * Searches this.dangerChars for ch.
             * TODO: Use binary search.
             */
            if (this.dangerChars.indexOf(ch) >= 0)
                return false;
        }

        if (ch <= this.lastPrintable)
            return true;

        boolean printable = true;
        synchronized (this) {
            try {
                if (this.checkerWriter == null) {
                    this.checkerStream = new BAOutputStream(10);
                    this.checkerWriter = new OutputStreamWriter(this.checkerStream, this.javaName);
                }

                if (ch > 0xffff) {
                    this.checkerWriter.write(((ch-0x10000)>>10)+0xd800);
                    this.checkerWriter.write(((ch-0x10000)&0x3ff)+0xdc00);
                    byte[] result = this.checkerStream.getBuffer();
                    if (this.checkerStream.size() == 2 && result[0] == '?' && result[1] == '?')
                        printable = false;
                } else {
                    this.checkerWriter.write(ch);
                    this.checkerWriter.flush();
                    byte[] result = this.checkerStream.getBuffer();
                    if (this.checkerStream.size() == 1 && result[0] == '?')
                        printable = false;
                }
                this.checkerStream.reset();
            } catch (IOException ioe) {
                printable = false;
            }
        }

        return printable;
    }

    /**
     * Why don't we use the original ByteArrayOutputStream?
     * - Because the toByteArray() method of the ByteArrayOutputStream
     * creates new byte[] instances for each call.
     */
    static class BAOutputStream extends ByteArrayOutputStream {
        BAOutputStream() {
            super();
        }

        BAOutputStream(int size) {
            super(size);
        }

        byte[] getBuffer() {
            return this.buf;
        }
    }

}
