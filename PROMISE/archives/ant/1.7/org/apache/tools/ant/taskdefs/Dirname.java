package org.apache.tools.ant.taskdefs;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * Determines the directory name of the specified file.
 *
 * This task can accept the following attributes:
 * <ul>
 * <li>file
 * <li>property
 * </ul>
 * Both <b>file</b> and <b>property</b> are required.
 * <p>
 * When this task executes, it will set the specified property to the
 * value of the specified file up to, but not including, the last path
 * element. If file is a file, the directory will be the current
 * directory.
 *
 *
 * @since Ant 1.5
 *
 * @ant.task category="property"
 */

public class Dirname extends Task {
    private File file;
    private String property;

    /**
     * Path to take the dirname of.
     * @param file a <code>File</code> value
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * The name of the property to set.
     * @param property the name of the property
     */
    public void setProperty(String property) {
        this.property = property;
    }


    /**
     * Execute this task.
     * @throws BuildException on error
     */
    public void execute() throws BuildException {
        if (property == null) {
            throw new BuildException("property attribute required", getLocation());
        }
        if (file == null) {
            throw new BuildException("file attribute required", getLocation());
        } else {
            String value = file.getParent();
            getProject().setNewProperty(property, value);
        }
    }
}

