package org.apache.tools.ant;

/**
 * Enables a task to control unknown attributes.
 *
 * @since Ant 1.7
 */
public interface DynamicAttributeNS {

    /**
     * Set a named attribute to the given value
     *
     * @param uri The namespace uri for this attribute, "" is
     *            used if there is no namespace uri.
     * @param localName The localname of this attribute.
     * @param qName The qualified name for this attribute
     * @param value The value of this attribute.
     * @throws BuildException when any error occurs
     */
    void setDynamicAttribute(
        String uri, String localName, String qName, String value)
            throws BuildException;

}
