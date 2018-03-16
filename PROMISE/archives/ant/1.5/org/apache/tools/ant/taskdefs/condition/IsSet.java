package org.apache.tools.ant.taskdefs.condition;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;

/**
 * Condition that tests whether a given property has been set.
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 * @since Ant 1.5
 * @version $Revision: 274041 $
 */
public class IsSet extends ProjectComponent implements Condition {
    private String property;

    public void setProperty(String p) {property = p;}

    public boolean eval() throws BuildException {
        if (property == null) {
            throw new BuildException("No property specified for isset "
                                     + "condition");
        }
        
        return getProject().getProperty(property) != null;
    }

}
