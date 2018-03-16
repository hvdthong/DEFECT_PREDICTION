package org.apache.tools.ant.taskdefs.condition;

import org.apache.tools.ant.BuildException;

/**
 * Condition that tests the OS type.
 *
 * @author <a href="mailto:stefan.bodewig@epost.de>Stefan Bodewig</a>
 * @version $Revision: 269456 $
 */
public class Os implements Condition {
    private String family;

    public void setFamily(String f) {family = f.toLowerCase();}

    public boolean eval() throws BuildException {
        String osName = System.getProperty("os.name").toLowerCase();
        String pathSep = System.getProperty("path.separator");
        if (family != null) {
            if (family.equals("windows")) {
                return osName.indexOf("windows") > -1;
            } else if (family.equals("dos")) {
                return pathSep.equals(";");
            } else if (family.equals("mac")) {
                return osName.indexOf("mac") > -1;
            } else if (family.equals("unix")) {
                return pathSep.equals(":")
                    && (!osName.startsWith("mac") || osName.endsWith("x"));
            }
            throw new BuildException("Don\'t know how to detect os family \""
                                     + family + "\"");
        }
        return false;
    }

}
