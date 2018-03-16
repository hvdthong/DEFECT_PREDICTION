package org.apache.tools.ant.taskdefs.optional.depend.constantpool;

import java.io.*;
import java.util.*;

/**
 * A NameAndType CP Info
 * 
 * @author Conor MacNeill
 */
public class NameAndTypeCPInfo extends ConstantPoolEntry {

    /**
     * Constructor.
     * 
     */
    public NameAndTypeCPInfo() {
        super(CONSTANT_NameAndType, 1);
    }

    /**
     * read a constant pool entry from a class stream.
     * 
     * @param cpStream the DataInputStream which contains the constant pool entry to be read.
     * 
     * @throws IOException if there is a problem reading the entry from the stream.
     */
    public void read(DataInputStream cpStream) throws IOException {
        nameIndex = cpStream.readUnsignedShort();
        descriptorIndex = cpStream.readUnsignedShort();
    } 

    /**
     * Print a readable version of the constant pool entry.
     * 
     * @return the string representation of this constant pool entry.
     */
    public String toString() {
        String value;

        if (isResolved()) {
            value = "Name = " + name + ", type = " + type;
        } else {
            value = "Name index = " + nameIndex + ", descriptor index = " + descriptorIndex;
        } 

        return value;
    } 

    /**
     * Resolve this constant pool entry with respect to its dependents in
     * the constant pool.
     * 
     * @param constantPool the constant pool of which this entry is a member
     * and against which this entry is to be resolved.
     */
    public void resolve(ConstantPool constantPool) {
        name = ((Utf8CPInfo) constantPool.getEntry(nameIndex)).getValue();
        type = ((Utf8CPInfo) constantPool.getEntry(descriptorIndex)).getValue();

        super.resolve(constantPool);
    } 

    public String getName() {
        return name;
    } 

    public String getType() {
        return type;
    } 

    private String name;
    private String type;
    private int    nameIndex;
    private int    descriptorIndex;
}

