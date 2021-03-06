package org.apache.tools.ant.taskdefs.optional.depend.constantpool;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * An Integer CP Info
 *
 */
public class IntegerCPInfo extends ConstantCPInfo {

    /** Constructor.  */
    public IntegerCPInfo() {
        super(CONSTANT_INTEGER, 1);
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
        setValue(new Integer(cpStream.readInt()));
    }

    /**
     * Print a readable version of the constant pool entry.
     *
     * @return the string representation of this constant pool entry.
     */
    public String toString() {
        return "Integer Constant Pool Entry: " + getValue();
    }

}

