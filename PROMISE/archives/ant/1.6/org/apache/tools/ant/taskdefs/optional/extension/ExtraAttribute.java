package org.apache.tools.ant.taskdefs.optional.extension;

import org.apache.tools.ant.BuildException;

/**
 * Simple holder for extra attributes in main section of manifest.
 *
 * @todo Refactor this and all the other parameter, sysproperty,
 *   property etc into a single class in framework
 */
public class ExtraAttribute {
    private String name;
    private String value;

    /**
     * Set the name of the parameter.
     *
     * @param name the name of parameter
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Set the value of the parameter.
     *
     * @param value the parameter value
     */
    public void setValue(final String value) {
        this.value = value;
    }

    /**
     * Retrieve name of parameter.
     *
     * @return the name of parameter.
     */
    String getName() {
        return name;
    }

    /**
     * Retrieve the value of parameter.
     *
     * @return the value of parameter.
     */
    String getValue() {
        return value;
    }

    /**
     * Make sure that neither the name or the value
     * is null.
     *
     * @throws BuildException if the attribute is invalid.
     */
    public void validate() throws BuildException {
        if (null == name) {
            final String message = "Missing name from parameter.";
            throw new BuildException(message);
        } else if (null == value) {
            final String message = "Missing value from parameter " + name + ".";
            throw new BuildException(message);
        }
    }
}
