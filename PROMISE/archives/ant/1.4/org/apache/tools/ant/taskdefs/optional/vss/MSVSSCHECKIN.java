package org.apache.tools.ant.taskdefs.optional.vss;

import org.apache.tools.ant.*;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Path;

import java.io.File;

/**
 * Task to perform CheckIn commands to Microsoft Visual Source Safe.
 *
 * @author Martin Poeschl
 */
public class MSVSSCHECKIN extends MSVSS {

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

        if (getVsspath() == null) {
            String msg = "vsspath attribute must be set!";
            throw new BuildException(msg, location);
        }


        commandLine.setExecutable(getSSCommand());
        commandLine.createArgument().setValue(COMMAND_CHECKIN);

        commandLine.createArgument().setValue(getVsspath());
        getLocalpathCommand(commandLine);
        getAutoresponse(commandLine);
        getRecursiveCommand(commandLine);
        getWritableCommand(commandLine);
        getLoginCommand(commandLine);
        commandLine.createArgument().setValue("-C"+getComment());

        result = run(commandLine);
        if ( result != 0 ) {
            String msg = "Failed executing: " + commandLine.toString();
            throw new BuildException(msg, location);
        }
    }

    /**
     * Set the local path.
     */
    public void setLocalpath(Path localPath) {
        m_LocalPath = localPath.toString();
    }

    /**
     * Builds and returns the -GL flag command if required
     * <p>
     * The localpath is created if it didn't exist
     */
    public void getLocalpathCommand(Commandline cmd) {
        if (m_LocalPath == null) {
            return;
        } else {
            File dir = project.resolveFile(m_LocalPath);
            if (!dir.exists()) {
                boolean done = dir.mkdirs();
                if (done == false) {
                    String msg = "Directory " + m_LocalPath + " creation was not " +
                        "succesful for an unknown reason";
                    throw new BuildException(msg, location);
                }
                project.log("Created dir: " + dir.getAbsolutePath());
            }

            cmd.createArgument().setValue(FLAG_OVERRIDE_WORKING_DIR + m_LocalPath);
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
        if ( !m_Recursive ) {
            return;
        } else {
            cmd.createArgument().setValue(FLAG_RECURSION);
        }
    }

    /**
     * Set behaviour, used in get command to make files that are 'got' writable
     */
    public final void setWritable(boolean argWritable) {
        m_Writable = argWritable;
    }

    /**
     * @return the 'make writable' command if the attribute was 'true', otherwise an empty string
     */
    public void getWritableCommand(Commandline cmd) {
        if ( !m_Writable ) {
            return;
        } else {
            cmd.createArgument().setValue(FLAG_WRITABLE);
        }
    }

    public void setAutoresponse(String response){
        if ( response.equals("") || response.equals("null") ) {
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

        if ( m_AutoResponse == null) {
            cmd.createArgument().setValue(FLAG_AUTORESPONSE_DEF);
        } else if ( m_AutoResponse.equalsIgnoreCase("Y")) {
            cmd.createArgument().setValue(FLAG_AUTORESPONSE_YES);

        } else if ( m_AutoResponse.equalsIgnoreCase("N")) {
            cmd.createArgument().setValue(FLAG_AUTORESPONSE_NO);
        }else {
            cmd.createArgument().setValue(FLAG_AUTORESPONSE_DEF);

    }

    /**
     * Set the comment to apply in SourceSafe
     * <p>
     * If this is null or empty, it will be replaced with "-" which
     * is what SourceSafe uses for an empty comment.
     */
    public void setComment(String comment) {
        if ( comment.equals("") || comment.equals("null") ) {
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
}
