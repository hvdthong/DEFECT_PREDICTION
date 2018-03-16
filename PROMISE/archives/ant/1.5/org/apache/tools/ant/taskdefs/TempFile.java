package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.FileUtils;

/**
 *  This task sets a property to  the name of a temporary file. 
 *  Unlike the Java1.2 method to create a temporary file, this task
 *  does work on Java1.1. Also, it does not actually create the
 *  temporary file, but it does guarantee that the file did not
 *  exist when the task was executed. 
 * <p>  
 * Examples
 * <pre>&lt;tempfile property="temp.file" /&gt;</pre>
 * create a temporary file
 * <pre>&lt;tempfile property="temp.file" suffix=".xml" /&gt;</pre>
 * create a temporary file with the .xml suffix.
 * <pre>&lt;tempfile property="temp.file" destDir="build"/&gt;</pre>
 * create a temp file in the build subdir
 *@author      steve loughran
 *@since       Ant 1.5
 *@ant.task
 */

public class TempFile extends Task {

    /**
     *  name of property to set
     */
    private String property;

    /**
     *  directory to create the file in. can be null
     */
    private File destDir = null;

    /**
     *  prefix for the file
     */
    private String prefix;

    /**
     *  suffix for the file
     */
    private String suffix = "";


    /**
     *  The property you wish to assign the temporary file to
     *
     *@param  property  The property to set
     */
    public void setProperty(String property) {
        this.property = property;
    }


    /**
     *  destination directory. If null, 
     the parent directory is used instead
     *
     *@param  destDir  The new destDir value
     */
    public void setDestDir(File destDir) {
        this.destDir = destDir;
    }


    /**
     *  optional prefix string
     *
     *@param  prefix  string to prepend to generated string
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }


    /**
     *  Suffix string for the temp file (optional)
     *
     *@param  suffix  suffix including any "." , e.g ".xml"
     */
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }


    /**
     *  create the temp file
     *
     *@exception  BuildException  if something goes wrong with the build
     */
    public void execute() throws BuildException {
        if (property == null || property.length() == 0) {
            throw new BuildException("no property specified");
        }
        if (destDir == null) {
            destDir = project.resolveFile(".");
        }
        FileUtils utils = FileUtils.newFileUtils();
        File tfile = utils.createTempFile(prefix, suffix, destDir);
        project.setNewProperty(property, tfile.toString());
    }
}
