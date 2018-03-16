package org.apache.xml.serialize;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

/**
 * This class represents an encoding.
 *
 * @version $Id: EncodingInfo.java 316729 2000-12-14 19:21:56Z lehors $
 */
public class EncodingInfo {

    String name;
    String javaName;
    int lastPrintable;

    /**
     * Creates new <code>EncodingInfo</code> instance.
     */
    public EncodingInfo(String mimeName, String javaName, int lastPrintable) {
        this.name = mimeName;
        this.javaName = javaName == null ? mimeName : javaName;
        this.lastPrintable = lastPrintable;
    }

    /**
     * Creates new <code>EncodingInfo</code> instance.
     */
    public EncodingInfo(String mimeName, int lastPrintable) {
        this(mimeName, mimeName, lastPrintable);
    }

    /**
     * Returns a MIME charset name of this encoding.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns a writer for this encoding based on
     * an output stream.
     *
     * @return A suitable writer
     * @exception UnsupportedEncodingException There is no convertor
     *  to support this encoding
     */
    public Writer getWriter(OutputStream output)
        throws UnsupportedEncodingException {
        if (this.javaName == null)
            return new OutputStreamWriter(output);
        return new OutputStreamWriter(output, this.javaName);
    }
    /**
     * Checks whether the specified character is printable or not.
     *
     * @param ch a code point (0-0x10ffff)
     */
    public boolean isPrintable(int ch) {
        return ch <= this.lastPrintable;
    }
}
