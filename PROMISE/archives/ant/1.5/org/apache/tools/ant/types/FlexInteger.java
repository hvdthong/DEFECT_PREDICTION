package org.apache.tools.ant.types;

/**
 * Helper class which can be used for Ant task attribute setter methods to allow
 * the build file to specify an integer in either decimal, octal, or hexadecimal
 * format.
 *
 * @author Erik Hatcher
 * @see java.lang.Integer#decode(String)
 */
public class FlexInteger {
    private Integer value;

    /**
     * Constructor used by Ant's introspection mechanism for attribute population
     */
    public FlexInteger(String value) {
        this.value = Integer.decode(value);
    }

    /**
     * Returns the decimal integer value
     */
    public int intValue() {
        return value.intValue();
    }

    /**
     * Overridden method to return the decimal value for display
     */
    public String toString() {
        return value.toString();
    }
}
