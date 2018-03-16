package org.apache.tools.ant;

/**
 * Enables a task to control unknown attributes
 *
 * @since Ant 1.5
 */
public interface DynamicAttribute {

    /**
     * Set a named attribute to the given value
     *
     * @param name the name of the attribute
     * @param value the new value of the attribute
     * @throws BuildException when any error occurs
     */
    void setDynamicAttribute(String name, String value)
            throws BuildException;

}
