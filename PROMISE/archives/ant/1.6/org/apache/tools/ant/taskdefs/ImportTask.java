package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.util.FileUtils;

import java.io.File;
import java.util.Vector;

/**
 * <p>
 * Task to import another build file into the current project.
 * <p>
 * It must be 'top level'. On execution it will read another Ant file
 * into the same Project.
 * <p>
 * <b>Important</b>: we have not finalized how relative file references
 * will be resolved in deep/complex build hierarchies -such as what happens
 * when an imported file imports another file. Use absolute references for
 * enhanced build file stability, especially in the imported files.
 *
 * Examples
 * <pre>
 * &lt;import file="../common-targets.xml" /&gt;
 * </pre>
 * Import targets from a file in a parent directory.
 *<p>
 * <pre>
 * &lt;import file="${deploy-platform}.xml" /&gt;
 * </pre>
 * Import the project defined by the property deploy-platform
 *
 * @since Ant1.6
 * @ant.task category="control"
 */
public class ImportTask extends Task {
    private String file;
    private boolean optional;
    private static final FileUtils FILE_UTILS = FileUtils.newFileUtils();

    /**
     * sets the optional attribute
     *
     * @param optional if true ignore files that are not present,
     *                 default is false
     */
    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    /**
     * the name of the file to import. How relative paths are resolved is still
     * in flux: use absolute paths for safety.
     * @param file the name of the file
     */
    public void setFile(String file) {
        this.file = file;
    }

    /**
     *  This relies on the task order model.
     *
     */
    public void execute() {
        if (file == null) {
            throw new BuildException("import requires file attribute");
        }
        if (getOwningTarget() == null
            || !"".equals(getOwningTarget().getName())) {
            throw new BuildException("import only allowed as a top-level task");
        }

        ProjectHelper helper =
                (ProjectHelper) getProject().getReference("ant.projectHelper");
        Vector importStack = helper.getImportStack();

        if (importStack.size() == 0) {
            throw new BuildException("import requires support in ProjectHelper");
        }

        if (getLocation() == null || getLocation().getFileName() == null) {
            throw new BuildException("Unable to get location of import task");
        }

        File buildFile = new File(getLocation().getFileName());
        buildFile = new File(buildFile.getAbsolutePath());

        getProject().log("Importing file " + file + " from "
                         + buildFile.getAbsolutePath(), Project.MSG_VERBOSE);


        File buildFileParent = new File(buildFile.getParent());
        File importedFile = FILE_UTILS.resolveFile(buildFileParent,  file);

        if (!importedFile.exists()) {
            String message =
                "Cannot find " + file + " imported from "
                + buildFile.getAbsolutePath();
            if (optional) {
                getProject().log(message, Project.MSG_VERBOSE);
                return;
            } else {
                throw new BuildException(message);
            }
        }

        if (importStack.contains(importedFile)) {
            getProject().log(
                "Skipped already imported file:\n   "
                + importedFile + "\n", Project.MSG_VERBOSE);
            return;
        }

        try {
            helper.parse(getProject(), importedFile);
        } catch (BuildException ex) {
            throw ProjectHelper.addLocationToBuildException(
                ex, getLocation());
        }
    }

}
