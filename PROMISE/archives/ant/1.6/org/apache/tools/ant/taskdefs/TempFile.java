package org.apache.tools.ant.taskdefs;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
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
 *@since       Ant 1.5
 *@ant.task
 */

public class TempFile extends Task {

    /**
     * Name of property to set.
     */
    private String property;

    /**
     * Directory to create the file in. Can be null.
     */
    private File destDir = null;

    /**
     * Prefix for the file.
     */
    private String prefix;

    /**
     * Suffix for the file.
     */
    private String suffix = "";


    /**
     * Sets the property you wish to assign the temporary file to.
     *
     * @param  property  The property to set
     * @ant.attribute group="required"
     */
    public void setProperty(String property) {
        this.property = property;
    }


    /**
     * Sets the destination directory. If not set,
     * the basedir directory is used instead.
     *
     * @param  destDir  The new destDir value
     */
    public void setDestDir(File destDir) {
        this.destDir = destDir;
    }


    /**
     * Sets the optional prefix string for the temp file.
     *
     * @param  prefix  string to prepend to generated string
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }


    /**
     * Sets the optional suffix string for the temp file.
     *
     * @param  suffix  suffix including any "." , e.g ".xml"
     */
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }


    /**
     * Creates the temporary file.
     *
     *@exception  BuildException  if something goes wrong with the build
     */
    public void execute() throws BuildException {
        if (property == null || property.length() == 0) {
            throw new BuildException("no property specified");
        }
        if (destDir == null) {
            destDir = getProject().resolveFile(".");
        }
        FileUtils utils = FileUtils.newFileUtils();
        File tfile = utils.createTempFile(prefix, suffix, destDir);
        getProject().setNewProperty(property, tfile.toString());
    }
}
