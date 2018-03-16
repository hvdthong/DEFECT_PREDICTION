package org.apache.tools.ant.taskdefs.optional.ccm;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Commandline;

/**
 * Class common to all check commands (checkout, checkin,checkin default task);
 * @author Benoit Moussaud benoit.moussaud@criltelecom.com
 * @ant.task ignore="true"
 */
public class CCMCheck extends Continuus {

    private File file = null;
    private String comment = null;
    private String task = null;

    public CCMCheck() {
        super();
    }

    /**
     * Get the value of file.
     * @return value of file.
     */
    public File getFile() {
        return file;
    }

    /**
     * Sets the path to the file that the command will operate on.
     * @param v  Value to assign to file.
     */
    public void setFile(File v) {
        this.file = v;
    }

    /**
     * Get the value of comment.
     * @return value of comment.
     */
    public String getComment() {
        return comment;
    }

    /**
     * Specifies a comment.
     * @param v  Value to assign to comment.
     */
    public void setComment(String v) {
        this.comment = v;
    }


    /**
     * Get the value of task.
     * @return value of task.
     */
    public String getTask() {
        return task;
    }

    /**
     * Specifies the task number used to check
     * in the file (may use 'default').
     * @param v  Value to assign to task.
     */
    public void setTask(String v) {
        this.task = v;
    }


    /**
     * Executes the task.
     * <p>
     * Builds a command line to execute ccm and then calls Exec's run method
     * to execute the command line.
     * </p>
     */
    public void execute() throws BuildException {
        Commandline commandLine = new Commandline();
        Project aProj = getProject();
        int result = 0;

        commandLine.setExecutable(getCcmCommand());
        commandLine.createArgument().setValue(getCcmAction());

        checkOptions(commandLine);

        result = run(commandLine);
        if (result != 0) {
            String msg = "Failed executing: " + commandLine.toString();
            throw new BuildException(msg, location);
        }
    }


    /**
     * Check the command line options.
     */
    private void checkOptions(Commandline cmd) {
        if (getComment() != null) {
            cmd.createArgument().setValue(FLAG_COMMENT);
            cmd.createArgument().setValue(getComment());
        }

        if (getTask() != null) {
            cmd.createArgument().setValue(FLAG_TASK);
            cmd.createArgument().setValue(getTask());

        if (getFile() != null) {
            cmd.createArgument().setValue(file.getAbsolutePath());
    }

    /**
     * -comment flag -- comment to attach to the file
     */
    public static final String FLAG_COMMENT = "/comment";

    /**
     *  -task flag -- associate checckout task with task
     */
    public static final String FLAG_TASK = "/task";
}

