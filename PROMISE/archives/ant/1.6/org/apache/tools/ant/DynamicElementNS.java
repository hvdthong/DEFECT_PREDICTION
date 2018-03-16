package org.apache.tools.ant;

/**
 * Enables a task to control unknown elements.
 *
 * @since Ant 1.7
 */
public interface DynamicElementNS {
    /**
     * Create an element with the given name
     *
     * @param uri The namespace uri for this attribute.
     * @param localName The localname of this attribute.
     * @param qName The qualified name for this element.
     * @throws BuildException when any error occurs
     * @return the element created for this element.
     */
    Object createDynamicElement(
        String uri, String localName, String qName) throws BuildException;
}
