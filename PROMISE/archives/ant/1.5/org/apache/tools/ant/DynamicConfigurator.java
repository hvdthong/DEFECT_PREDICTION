package org.apache.tools.ant;

/**
 * Enables a task to control unknown attributes and elements.
 *
 * @author Erik Hatcher
 * @since Ant 1.5
 */
public interface DynamicConfigurator {
    
    /**
     * Set a named attribute to the given value
     * 
     * @param name the name of the attribute
     * @param value the new value of the attribute
     * @throws BuildException when any error occurs
     */    
    public void setDynamicAttribute(String name, String value)
            throws BuildException;

    /**
     * Create an element with the given name
     *
     * @param name the element nbame
     * @throws BuildException when any error occurs
     * @return the element created
     */    
    public Object createDynamicElement(String name) throws BuildException;
}
