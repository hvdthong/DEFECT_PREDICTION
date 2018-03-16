package org.apache.tools.ant.types;

import org.apache.tools.ant.BuildException;

/**
 * Helper class for attributes that can only take one of a fixed list
 * of values.
 *
 * <p>See {@link org.apache.tools.ant.taskdefs.FixCRLF FixCRLF} for an
 * example.
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a> 
 */
public abstract class EnumeratedAttribute {

    protected String value;

    /**
     * This is the only method a subclass needs to implement.
     *
     * @return an array holding all possible values of the enumeration.
     */
    public abstract String[] getValues();

    public EnumeratedAttribute() {}

    /**
     * Invoked by {@link org.apache.tools.ant.IntrospectionHelper IntrospectionHelper}.
     */
    public final void setValue(String value) throws BuildException {
        if (!containsValue(value)) {
            throw new BuildException(value+" is not a legal value for this attribute");
        }
        this.value = value;
    }

    /**
     * Is this value included in the enumeration?
     */
    public final boolean containsValue(String value) {
        String[] values = getValues();
        if (values == null || value == null) {
            return false;
        }
        
        for (int i=0; i<values.length; i++) {
            if (value.equals(values[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves the value.
     */
    public final String getValue() {
        return value;
    }
}
