package org.apache.tools.ant.taskdefs.optional.clearcase;

import org.apache.tools.ant.*;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Path;

import java.io.File;
import java.lang.System;

/**
 * Task to perform an Update command to ClearCase.
 * <p>
 * The following attributes are interpretted:
 * <table border="1">
 *   <tr>
 *     <th>Attribute</th>
 *     <th>Values</th>
 *     <th>Required</th>
 *   </tr>
 *   <tr>
 *      <td>viewpath</td>
 *      <td>Path to the ClearCase view file or directory that the command will operate on</td>
 *      <td>No</td>
 *   <tr>
 *   <tr>
 *      <td>graphical</td>
 *      <td>Displays a graphical dialog during the update</td>
 *      <td>No</td>
 *   <tr>
 *   <tr>
 *      <td>log</td>
 *      <td>Specifies a log file for ClearCase to write to</td>
 *      <td>No</td>
 *   <tr>
 *   <tr>
 *      <td>overwrite</td>
 *      <td>Specifies whether to overwrite hijacked files or not</td>
 *      <td>No</td>
 *   <tr>
 *   <tr>
 *      <td>rename</td>
 *      <td>Specifies that hijacked files should be renamed with a .keep extension</td>
 *      <td>No</td>
 *   <tr>
 *   <tr>
 *      <td>currenttime</td>
 *      <td>Specifies that modification time should be written as the current time. Either currenttime or preservetime can be specified.</td>
 *      <td>No</td>
 *   <tr>
 *   <tr>
 *      <td>preservetime</td>
 *      <td>Specifies that modification time should preserved from the VOB time. Either currenttime or preservetime can be specified.</td>
 *      <td>No</td>
 *   <tr>
 * </table>
 *
 * @author Curtis White
 */
public class CCUpdate extends ClearCase {
    private boolean m_Graphical = false;
    private boolean m_Overwrite = false;
    private boolean m_Rename = false;
    private boolean m_Ctime = false;
    private boolean m_Ptime = false;
    private String m_Log = null;

    /**
     * Executes the task.
     * <p>
     * Builds a command line to execute cleartool and then calls Exec's run method
     * to execute the command line.
     */
    public void execute() throws BuildException {
        Commandline commandLine = new Commandline();
        Project aProj = getProject();
        int result = 0;

        if (getViewPath() == null) {
            setViewPath(aProj.getBaseDir().getPath());
        }

        commandLine.setExecutable(getClearToolCommand());
        commandLine.createArgument().setValue(COMMAND_UPDATE);

        checkOptions(commandLine);

        System.out.println(commandLine.toString());

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
        if (getGraphical()) {
            cmd.createArgument().setValue(FLAG_GRAPHICAL);
        } else {
            if (getOverwrite()) {
                cmd.createArgument().setValue(FLAG_OVERWRITE);
            } else {
                if (getRename()) {
                    cmd.createArgument().setValue(FLAG_RENAME);
                } else {
                    cmd.createArgument().setValue(FLAG_NOVERWRITE);
                }
            }

            if (getCurrentTime()) {
                cmd.createArgument().setValue(FLAG_CURRENTTIME);
            } else {
                if (getPreserveTime()) {
                    cmd.createArgument().setValue(FLAG_PRESERVETIME);
                }
            }

            getLogCommand(cmd);
        }

        cmd.createArgument().setValue(getViewPath());
    }

    /**
     * Set graphical flag status
     *
     * @param graphical the status to set the flag to
     */
    public void setGraphical(boolean graphical) {
        m_Graphical = graphical;
    }

    /**
     * Get graphical flag status
     *
     * @return boolean containing status of graphical flag
     */
    public boolean getGraphical() {
        return m_Graphical;
    }

    /**
     * Set overwrite hijacked files status
     *
     * @param ow the status to set the flag to
     */
    public void setOverwrite(boolean ow) {
        m_Overwrite = ow;
    }

    /**
     * Get overwrite hijacked files status
     *
     * @return boolean containing status of overwrite flag
     */
    public boolean getOverwrite() {
        return m_Overwrite;
    }

    /**
     * Set rename hijacked files status
     *
     * @param ren the status to set the flag to
     */
    public void setRename(boolean ren) {
        m_Rename = ren;
    }

    /**
     * Get rename hijacked files status
     *
     * @return boolean containing status of rename flag
     */
    public boolean getRename() {
        return m_Rename;
    }

    /**
     * Set modified time based on current time
     *
     * @param ct the status to set the flag to
     */
    public void setCurrentTime(boolean ct) {
        m_Ctime = ct;
    }

    /**
     * Get current time status
     *
     * @return boolean containing status of current time flag
     */
    public boolean getCurrentTime() {
        return m_Ctime;
    }

    /**
     * Preserve modified time from the VOB time
     *
     * @param pt the status to set the flag to
     */
    public void setPreserveTime(boolean pt) {
        m_Ptime = pt;
    }

    /**
     * Get preserve time status
     *
     * @return boolean containing status of preserve time flag
     */
    public boolean getPreserveTime() {
        return m_Ptime;
    }

    /**
     * Set log file where cleartool can record the status of the command
     *
     * @param log the path to the log file
     */
    public void setLog(String log) {
        m_Log = log;
    }

    /**
     * Get log file
     *
     * @return String containing the path to the log file
     */
    public String getLog() {
        return m_Log;
    }

    /**
     * Get the 'log' command
     *
     * @return the 'log' command if the attribute was specified, otherwise an empty string
     *
     * @param CommandLine containing the command line string with or without the log flag and path appended
     */
    private void getLogCommand(Commandline cmd) {
        if (getLog() == null) {
            return;
        } else {
            /* Had to make two separate commands here because if a space is
               inserted between the flag and the value, it is treated as a
               Windows filename with a space and it is enclosed in double
               quotes ("). This breaks clearcase.
            */
            cmd.createArgument().setValue(FLAG_LOG);
            cmd.createArgument().setValue(getLog());
        }
    }

        /**
     *  -graphical flag -- display graphical dialog during update operation
     */
    public static final String FLAG_GRAPHICAL = "-graphical";
        /**
     * -log flag -- file to log status to
     */
    public static final String FLAG_LOG = "-log";
        /**
     * -overwrite flag -- overwrite hijacked files
     */
    public static final String FLAG_OVERWRITE = "-overwrite";
        /**
     * -noverwrite flag -- do not overwrite hijacked files
     */
    public static final String FLAG_NOVERWRITE = "-noverwrite";
        /**
     * -rename flag -- rename hijacked files with .keep extension
     */
    public static final String FLAG_RENAME = "-rename";
        /**
     * -ctime flag -- modified time is written as the current time
     */
    public static final String FLAG_CURRENTTIME = "-ctime";
        /**
     * -ptime flag -- modified time is written as the VOB time
     */
    public static final String FLAG_PRESERVETIME = "-ptime";

}

