package org.apache.tools.ant.taskdefs.optional.depend.constantpool;

import java.io.*;
import java.util.*;

/**
 * The constant pool entry which stores class information.
 * 
 * @author Conor MacNeill
 */
public class ClassCPInfo extends ConstantPoolEntry {

    /**
     * The class' name. This will be only valid if the entry has been resolved
     * against the constant pool.
     */
    private String className;

    /**
     * The index into the constant pool where this class' name is stored. If the class
     * name is changed, this entry is invalid until this entry is connected to a constant
     * pool.
     */
    private int    index;

    /**
     * Constructor.
     * 
     * Sets the tag value for this entry to type Class
     */
    public ClassCPInfo() {
        super(CONSTANT_Class, 1);
    }

    /**
     * Read the entry from a stream.
     * 
     * @param cpStream the stream containing the constant pool entry to be read.
     * 
     * @exception IOException thrown if there is a problem reading the entry from the stream.
     */
    public void read(DataInputStream cpStream) throws IOException {
        index = cpStream.readUnsignedShort();
        className = "unresolved";
    } 

    /**
     * Generate a string readable version of this entry
     */
    public String toString() {
        return "Class Constant Pool Entry for " + className + "[" + index + "]";
    } 

    /**
     * Resolve this class info against the given constant pool.
     * 
     * @param constantPool the constant pool with which to resolve the class.
     */
    public void resolve(ConstantPool constantPool) {
        className = ((Utf8CPInfo) constantPool.getEntry(index)).getValue();

        super.resolve(constantPool);
    } 

    /**
     * Get the class name of this entry.
     * 
     * @return the class' name.
     */
    public String getClassName() {
        return className;
    } 

}

