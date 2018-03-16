package org.apache.tools.ant.taskdefs.optional.depend.constantpool;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * A Long CP Info
 *
 */
public class LongCPInfo extends ConstantCPInfo {

    /** Constructor.  */
    public LongCPInfo() {
        super(CONSTANT_LONG, 2);
    }

    /**
     * read a constant pool entry from a class stream.
     *
     * @param cpStream the DataInputStream which contains the constant pool
     *      entry to be read.
     * @exception IOException if there is a problem reading the entry from
     *      the stream.
     */
    public void read(DataInputStream cpStream) throws IOException {
        setValue(new Long(cpStream.readLong()));
    }

    /**
     * Print a readable version of the constant pool entry.
     *
     * @return the string representation of this constant pool entry.
     */
    public String toString() {
        return "Long Constant Pool Entry: " + getValue();
    }

}

