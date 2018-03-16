package org.apache.tools.ant.taskdefs.optional.ccm;

import org.apache.tools.ant.*;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Path;

import java.io.File;

/**
 * Class common to all check commands (checkout, checkin,checkin default task);
 * @author Benoit Moussaud benoit.moussaud@criltelecom.com
 */
public class CCMCheck extends Continuus {

    private File   _file    = null;
    private String _comment = null;
    private String _task    = null;

    public CCMCheck() {
        super();
    }

    /**
     * Get the value of file.
     * @return value of file.
     */
    public File getFile() {
        return _file;
    }
    
    /**
     * Set the value of file.
     * @param v  Value to assign to file.
     */
    public void setFile(File  v) {
        this._file = v;
    }
    
    /**
     * Get the value of comment.
     * @return value of comment.
     */
    public String getComment() {
        return _comment;
    }
    
    /**
     * Set the value of comment.
     * @param v  Value to assign to comment.
     */
    public void setComment(String  v) {
        this._comment = v;
    }

    
    /**
     * Get the value of task.
     * @return value of task.
     */
    public String getTask() {
        return _task;
    }
    
    /**
     * Set the value of task.
     * @param v  Value to assign to task.
     */
    public void setTask(String  v) {
        this._task = v;
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
        if ( result != 0 ) {
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

        if ( getTask() != null) {
            cmd.createArgument().setValue(FLAG_TASK);
            cmd.createArgument().setValue(getTask());            
        
        if ( getFile() != null ) {
            cmd.createArgument().setValue(_file.getAbsolutePath());       
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

