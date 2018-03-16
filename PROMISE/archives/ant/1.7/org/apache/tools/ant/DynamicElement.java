package org.apache.tools.ant;

/**
 * Enables a task to control unknown elements.
 *
 * @since Ant 1.5
 */
public interface DynamicElement {

    /**
     * Create an element with the given name
     *
     * @param name the element nbame
     * @throws BuildException when any error occurs
     * @return the element created
     */
    Object createDynamicElement(String name) throws BuildException;
}
