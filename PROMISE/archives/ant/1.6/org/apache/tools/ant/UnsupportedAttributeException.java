package org.apache.tools.ant;

/**
 * Used to report attempts to set an unsupported attribute
 *
 * @since Ant 1.6.3
 */
public class UnsupportedAttributeException extends BuildException {

    private String attribute;

    /**
     * Constructs an unsupported attribute exception.
     * @param msg       The string containing the message.
     * @param attribute The unsupported attribute.
     */
    public UnsupportedAttributeException(String msg, String attribute) {
        super(msg);
        this.attribute = attribute;
    }

    /**
     * Get the attribute that is wrong.
     *
     * @return the attribute name.
     */
    public String getAttribute() {
        return attribute;
    }

}
