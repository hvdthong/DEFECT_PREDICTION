package org.apache.tools.ant.taskdefs.optional.depend.constantpool;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * A Float CP Info
 *
 * @author Conor MacNeill
 */
public class FloatCPInfo extends ConstantCPInfo {

    /** Constructor.  */
    public FloatCPInfo() {
        super(CONSTANT_FLOAT, 1);
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
        setValue(new Float(cpStream.readFloat()));
    }

    /**
     * Print a readable version of the constant pool entry.
     *
     * @return the string representation of this constant pool entry.
     */
    public String toString() {
        return "Float Constant Pool Entry: " + getValue();
    }

}

