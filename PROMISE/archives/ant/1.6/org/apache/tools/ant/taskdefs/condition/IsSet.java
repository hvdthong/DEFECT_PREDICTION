package org.apache.tools.ant.taskdefs.condition;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;

/**
 * Condition that tests whether a given property has been set.
 *
 * @since Ant 1.5
 */
public class IsSet extends ProjectComponent implements Condition {
    private String property;

    /**
     * Set the property attribute
     *
     * @param p the property name
     */
    public void setProperty(String p) {
        property = p;
    }

    /**
     * @return true if the property exists
     * @exception BuildException if the property attribute is not set
     */
    public boolean eval() throws BuildException {
        if (property == null) {
            throw new BuildException("No property specified for isset "
                                     + "condition");
        }

        return getProject().getProperty(property) != null;
    }

}
