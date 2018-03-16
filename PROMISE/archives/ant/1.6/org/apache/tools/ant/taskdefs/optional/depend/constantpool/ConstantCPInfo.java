package org.apache.tools.ant.taskdefs.optional.depend.constantpool;

/**
 * A Constant Pool entry which represents a constant value.
 *
 */
public abstract class ConstantCPInfo extends ConstantPoolEntry {

    /**
     * The entry's untyped value. Each subclass interprets the constant
     * value based on the subclass's type. The value here must be
     * compatible.
     */
    private Object value;

    /**
     * Initialise the constant entry.
     *
     * @param tagValue the constant pool entry type to be used.
     * @param entries the number of constant pool entry slots occupied by
     *      this entry.
     */
    protected ConstantCPInfo(int tagValue, int entries) {
        super(tagValue, entries);
    }

    /**
     * Get the value of the constant.
     *
     * @return the value of the constant (untyped).
     */
    public Object getValue() {
        return value;
    }

    /**
     * Set the constant value.
     *
     * @param newValue the new untyped value of this constant.
     */
    public void setValue(Object newValue) {
        value = newValue;
    }

}

