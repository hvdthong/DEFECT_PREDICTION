package org.apache.tools.ant;

/**
 * Used to report attempts to set an unsupported element
 * When the attempt to set the element is made,
 * the code does not not know the name of the task/type
 * based on a mapping from the classname to the task/type.
 * However one class may be used by a lot of task/types.
 * This exception may be caught by code that does know
 * the task/type and it will reset the message to the
 * correct message.
 * This will be done once (in the case of a recursive
 * call to handlechildren).
 *
 * @since Ant 1.6.3
 */
public class UnsupportedElementException extends BuildException {

    private String element;

    /**
     * Constructs an unsupported element exception.
     * @param msg The string containing the message.
     * @param element The name of the incorrect element.
     */
    public UnsupportedElementException(String msg, String element) {
        super(msg);
        this.element = element;
    }

    /**
     * Get the element that is wrong.
     *
     * @return the element name.
     */
    public String getElement() {
        return element;
    }
}
