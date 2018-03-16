package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.*;
import org.apache.tools.ant.types.*;

import java.io.*;
import java.util.*;

/**
 * Chmod equivalent for unix-like environments.
 *
 * @author costin@eng.sun.com
 * @author Mariusz Nowostawski (Marni) <a href="mailto:mnowostawski@infoscience.otago.ac.nz">mnowostawski@infoscience.otago.ac.nz</a>
 * @author <a href="mailto:stefan.bodewig@megabit.net">Stefan Bodewig</a>
 */

public class Chmod extends ExecuteOn {

    private FileSet defaultSet = new FileSet();
    private boolean havePerm = false;
    
    public Chmod() {
        super.setExecutable("chmod");
        super.setParallel(true);
    }

    public void setFile(File src) {
        FileSet fs = new FileSet();
        fs.setDir(new File(src.getParent()));
        fs.createInclude().setName(src.getName());
        addFileset(fs);
    }

    public void setDir(File src) {
        defaultSet.setDir(src);
    }

    public void setPerm(String perm) {
        createArg().setValue(perm);
        havePerm = true;
    }

    /**
     * add a name entry on the include list
     */
    public PatternSet.NameEntry createInclude() {
        return defaultSet.createInclude();
    }
    
    /**
     * add a name entry on the exclude list
     */
    public PatternSet.NameEntry createExclude() {
        return defaultSet.createExclude();
    }

    /**
     * add a set of patterns
     */
    public PatternSet createPatternSet() {
        return defaultSet.createPatternSet();
    }

    /**
     * Sets the set of include patterns. Patterns may be separated by a comma
     * or a space.
     *
     * @param includes the string containing the include patterns
     */
    public void setIncludes(String includes) {
        defaultSet.setIncludes(includes);
    }

    /**
     * Sets the set of exclude patterns. Patterns may be separated by a comma
     * or a space.
     *
     * @param excludes the string containing the exclude patterns
     */
    public void setExcludes(String excludes) {
        defaultSet.setExcludes(excludes);
    }

    /**
     * Sets whether default exclusions should be used or not.
     *
     * @param useDefaultExcludes "true"|"on"|"yes" when default exclusions 
     *                           should be used, "false"|"off"|"no" when they
     *                           shouldn't be used.
     */
    public void setDefaultexcludes(boolean useDefaultExcludes) {
        defaultSet.setDefaultexcludes(useDefaultExcludes);
    }
    
    protected void checkConfiguration() {
        if (!havePerm) {
            throw new BuildException("Required attribute perm not set in chmod", 
                                     location);
        }
        
        if (defaultSet.getDir(project) != null) {
            addFileset(defaultSet);
        }
        super.checkConfiguration();
    }

    public void setExecutable(String e) {
        throw new BuildException(taskType+" doesn\'t support the executable attribute", location);
    }

    public void setCommand(String e) {
        throw new BuildException(taskType+" doesn\'t support the command attribute", location);
    }

    protected boolean isValidOs() {
        return System.getProperty("path.separator").equals(":") 
            && (!System.getProperty("os.name").startsWith("Mac") 
                 || System.getProperty("os.name").endsWith("X"))
            && super.isValidOs();
    }
}
