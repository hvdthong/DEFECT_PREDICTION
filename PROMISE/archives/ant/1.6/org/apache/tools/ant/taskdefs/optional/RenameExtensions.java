package org.apache.tools.ant.taskdefs.optional;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.taskdefs.Move;
import org.apache.tools.ant.types.Mapper;

/**
 *
 * @version 1.2
 *
 * @deprecated Use &lt;move&gt; instead
 */
public class RenameExtensions extends MatchingTask {

    private String fromExtension = "";
    private String toExtension = "";
    private boolean replace = false;
    private File srcDir;

    private Mapper.MapperType globType;


    /** Creates new RenameExtensions */
    public RenameExtensions() {
        super();
        globType = new Mapper.MapperType();
        globType.setValue("glob");
    }

    /**
     * The string that files must end in to be renamed
     *
     * @param from the extension of files being renamed.
     */
    public void setFromExtension(String from) {
        fromExtension = from;
    }

    /**
     * The string that renamed files will end with on
     * completion
     *
     * @param to the extension of the renamed files.
     */
    public void setToExtension(String to) {
        toExtension = to;
    }

    /**
     * store replace attribute - this determines whether the target file
     * should be overwritten if present
     *
     * @param replace if true overwrite any target files that exist.
     */
    public void setReplace(boolean replace) {
        this.replace = replace;
    }

    /**
     * Set the source dir to find the files to be renamed.
     *
     * @param srcDir the source directory.
     */
    public void setSrcDir(File srcDir) {
        this.srcDir = srcDir;
    }

    /**
     * Executes the task.
     *
     * @throws BuildException is there is a problem in the task execution.
     */
    public void execute() throws BuildException {

        if (fromExtension == null || toExtension == null || srcDir == null) {
            throw new BuildException("srcDir, fromExtension and toExtension "
                + "attributes must be set!");
        }

        log("DEPRECATED - The renameext task is deprecated.  Use move instead.",
            Project.MSG_WARN);
        log("Replace this with:", Project.MSG_INFO);
        log("<move todir=\"" + srcDir + "\" overwrite=\"" + replace + "\">",
            Project.MSG_INFO);
        log("  <fileset dir=\"" + srcDir + "\" />", Project.MSG_INFO);
        log("  <mapper type=\"glob\"", Project.MSG_INFO);
        log("          from=\"*" + fromExtension + "\"", Project.MSG_INFO);
        log("          to=\"*" + toExtension + "\" />", Project.MSG_INFO);
        log("</move>", Project.MSG_INFO);
        log("using the same patterns on <fileset> as you\'ve used here",
            Project.MSG_INFO);

        Move move = (Move) getProject().createTask("move");
        move.setOwningTarget(getOwningTarget());
        move.setTaskName(getTaskName());
        move.setLocation(getLocation());
        move.setTodir(srcDir);
        move.setOverwrite(replace);

        fileset.setDir(srcDir);
        move.addFileset(fileset);

        Mapper me = move.createMapper();
        me.setType(globType);
        me.setFrom("*" + fromExtension);
        me.setTo("*" + toExtension);

        move.execute();
    }

}
