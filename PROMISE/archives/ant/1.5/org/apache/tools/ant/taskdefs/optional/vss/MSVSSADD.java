package org.apache.tools.ant.taskdefs.optional.vss;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Path;

/**
 * Performs Add commands to Microsoft Visual SourceSafe.
 * Based on the VSS Checkin code by Martin Poeschl
 *
 * @author Nigel Magnay
 * @ant.task name="vssadd" category="scm"
 */
public class MSVSSADD extends MSVSS {

    private String m_LocalPath = null;
    private boolean m_Recursive = false;
    private boolean m_Writable = false;
    private String m_AutoResponse = null;
    private String m_Comment = "-";

    /**
     * Executes the task.
     * <p>
     * Builds a command line to execute ss and then calls Exec's run method
     * to execute the command line.
     */
    public void execute() throws BuildException {
        Commandline commandLine = new Commandline();
        int result = 0;

        if (getLocalPath() == null) {
            String msg = "localPath attribute must be set!";
            throw new BuildException(msg, location);
        }


        commandLine.setExecutable(getSSCommand());
        commandLine.createArgument().setValue(COMMAND_ADD);

        commandLine.createArgument().setValue(getLocalPath());        
        getAutoresponse(commandLine);
        getRecursiveCommand(commandLine);
        getWritableCommand(commandLine);
        getLoginCommand(commandLine);
        commandLine.createArgument().setValue("-C" + getComment());

        result = run(commandLine);
        if (result != 0) {
            String msg = "Failed executing: " + commandLine.toString();
            throw new BuildException(msg, location);
        }
    }

    /**
     * Set behaviour recursive or non-recursive
     */
    public void setRecursive(boolean recursive) {
        m_Recursive = recursive;
    }

    /**
     * @return the 'recursive' command if the attribute was 'true', otherwise an empty string
     */
    public void getRecursiveCommand(Commandline cmd) {
        if (!m_Recursive) {
            return;
        } else {
            cmd.createArgument().setValue(FLAG_RECURSION);
        }
    }

    /**
     * Leave added files writable? Default: false. 
     */
    public final void setWritable(boolean argWritable) {
        m_Writable = argWritable;
    }

    /**
     * @return the 'make writable' command if the attribute was 'true', otherwise an empty string
     */
    public void getWritableCommand(Commandline cmd) {
        if (!m_Writable) {
            return;
        } else {
            cmd.createArgument().setValue(FLAG_WRITABLE);
        }
    }

    /**
     * What to respond with (sets the -I option). By default, -I- is
     * used; values of Y or N will be appended to this.
     */      
    public void setAutoresponse(String response){
        if (response.equals("") || response.equals("null")) {
            m_AutoResponse = null;
        } else {
            m_AutoResponse = response;
        }
    }

    /**
     * Checks the value set for the autoResponse.
     * if it equals "Y" then we return -I-Y
     * if it equals "N" then we return -I-N
     * otherwise we return -I
     */
    public void getAutoresponse(Commandline cmd) {

        if (m_AutoResponse == null) {
            cmd.createArgument().setValue(FLAG_AUTORESPONSE_DEF);
        } else if (m_AutoResponse.equalsIgnoreCase("Y")) {
            cmd.createArgument().setValue(FLAG_AUTORESPONSE_YES);

        } else if (m_AutoResponse.equalsIgnoreCase("N")) {
            cmd.createArgument().setValue(FLAG_AUTORESPONSE_NO);
        } else {
            cmd.createArgument().setValue(FLAG_AUTORESPONSE_DEF);

    }

    /**
     * Sets the comment to apply; optional.
     * <p>
     * If this is null or empty, it will be replaced with "-" which
     * is what SourceSafe uses for an empty comment.
     */
    public void setComment(String comment) {
        if (comment.equals("") || comment.equals("null")) {
            m_Comment = "-";
        } else {
            m_Comment = comment;
        }
    }

    /**
     * Gets the comment to be applied.
     * @return the comment to be applied.
     */
    public String getComment() {
        return m_Comment;
    }

    /**
     * Set the local path.
     */
    public void setLocalpath(Path localPath) {
        m_LocalPath = localPath.toString();
    }

    public String getLocalPath() {
        return m_LocalPath;
    }
}
