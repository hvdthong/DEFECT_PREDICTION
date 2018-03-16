package org.apache.tools.ant.taskdefs.email;

/**
 * Class representing a generic e-mail header.
 * @since Ant 1.7
 */
public class Header {
    private String name;
    private String value;

    /**
     * Set the name of this Header.
     * @param name the name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the name of this Header.
     * @return name as String.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the value of this Header.
     * @param value the value to set.
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Get the value of this Header.
     * @return value as String.
     */
    public String getValue() {
        return value;
    }

}
