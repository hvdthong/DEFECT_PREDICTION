package org.apache.tools.ant.taskdefs.optional.clearcase;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Commandline;




/**
 * Performs ClearCase checkin.
 *
 * <p>
 * The following attributes are interpreted:
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
 *      <td>comment</td>
 *      <td>Specify a comment. Only one of comment or cfile may be used.</td>
 *      <td>No</td>
 *   <tr>
 *   <tr>
 *      <td>commentfile</td>
 *      <td>Specify a file containing a comment. Only one of comment or cfile may be used.</td>
 *      <td>No</td>
 *   <tr>
 *   <tr>
 *      <td>nowarn</td>
 *      <td>Suppress warning messages</td>
 *      <td>No</td>
 *   <tr>
 *   <tr>
 *      <td>preservetime</td>
 *      <td>Preserve the modification time</td>
 *      <td>No</td>
 *   <tr>
 *   <tr>
 *      <td>keepcopy</td>
 *      <td>Keeps a copy of the file with a .keep extension</td>
 *      <td>No</td>
 *   <tr>
 *   <tr>
 *      <td>identical</td>
 *      <td>Allows the file to be checked in even if it is identical to the original</td>
 *      <td>No</td>
 *   <tr>
 * </table>
 *
 * @author Curtis White
 */
public class CCCheckin extends ClearCase {
    private String m_Comment = null;
    private String m_Cfile = null;
    private boolean m_Nwarn = false;
    private boolean m_Ptime = false;
    private boolean m_Keep = false;
    private boolean m_Identical = true;

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
        commandLine.createArgument().setValue(COMMAND_CHECKIN);

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
            getCommentCommand(cmd);
        } else {
            if (getCommentFile() != null) {
                getCommentFileCommand(cmd);
            } else {
                cmd.createArgument().setValue(FLAG_NOCOMMENT);
            }
        }

        if (getNoWarn()) {
            cmd.createArgument().setValue(FLAG_NOWARN);
        }

        if (getPreserveTime()) {
            cmd.createArgument().setValue(FLAG_PRESERVETIME);
        }

        if (getKeepCopy()) {
            cmd.createArgument().setValue(FLAG_KEEPCOPY);
        }

        if (getIdentical()) {
            cmd.createArgument().setValue(FLAG_IDENTICAL);
        }

        cmd.createArgument().setValue(getViewPath());
    }


    /**
     * Sets the comment string.
     *
     * @param comment the comment string
     */
    public void setComment(String comment) {
        m_Comment = comment;
    }

    /**
     * Get comment string
     *
     * @return String containing the comment
     */
    public String getComment() {
        return m_Comment;
    }

    /**
     * Specifies a file containing a comment.
     *
     * @param cfile the path to the comment file
     */
    public void setCommentFile(String cfile) {
        m_Cfile = cfile;
    }

    /**
     * Get comment file
     *
     * @return String containing the path to the comment file
     */
    public String getCommentFile() {
        return m_Cfile;
    }

    /**
     * If true, suppress warning messages.
     *
     * @param nwarn the status to set the flag to
     */
    public void setNoWarn(boolean nwarn) {
        m_Nwarn = nwarn;
    }

    /**
     * Get nowarn flag status
     *
     * @return boolean containing status of nwarn flag
     */
    public boolean getNoWarn() {
        return m_Nwarn;
    }

    /**
     * If true, preserve the modification time.
     *
     * @param ptime the status to set the flag to
     */
    public void setPreserveTime(boolean ptime) {
        m_Ptime = ptime;
    }

    /**
     * Get preservetime flag status
     *
     * @return boolean containing status of preservetime flag
     */
    public boolean getPreserveTime() {
        return m_Ptime;
    }

    /**
     * If true, keeps a copy of the file with a .keep extension.
     *
     * @param keep the status to set the flag to
     */
    public void setKeepCopy(boolean keep) {
        m_Keep = keep;
    }

    /**
     * Get keepcopy flag status
     *
     * @return boolean containing status of keepcopy flag
     */
    public boolean getKeepCopy() {
        return m_Keep;
    }

    /**
     * If true, allows the file to be checked in even
     * if it is identical to the original.
     *
     * @param identical the status to set the flag to
     */
    public void setIdentical(boolean identical) {
        m_Identical = identical;
    }

    /**
     * Get identical flag status
     *
     * @return boolean containing status of identical flag
     */
    public boolean getIdentical() {
        return m_Identical;
    }


    /**
     * Get the 'comment' command
     *
     * @param cmd containing the command line string with or without the comment flag and string appended
     */
    private void getCommentCommand(Commandline cmd) {
        if (getComment() != null) {
            /* Had to make two separate commands here because if a space is
               inserted between the flag and the value, it is treated as a
               Windows filename with a space and it is enclosed in double
               quotes ("). This breaks clearcase.
            */
            cmd.createArgument().setValue(FLAG_COMMENT);
            cmd.createArgument().setValue(getComment());
        }
    }

    /**
     * Get the 'commentfile' command
     *
     * @param cmd containing the command line string with or without the commentfile flag and file appended
     */
    private void getCommentFileCommand(Commandline cmd) {
        if (getCommentFile() != null) {
            /* Had to make two separate commands here because if a space is
               inserted between the flag and the value, it is treated as a
               Windows filename with a space and it is enclosed in double
               quotes ("). This breaks clearcase.
            */
            cmd.createArgument().setValue(FLAG_COMMENTFILE);
            cmd.createArgument().setValue(getCommentFile());
        }
    }


        /**
     * -c flag -- comment to attach to the file
     */
    public static final String FLAG_COMMENT = "-c";
        /**
     * -cfile flag -- file containing a comment to attach to the file
     */
    public static final String FLAG_COMMENTFILE = "-cfile";
        /**
     * -nc flag -- no comment is specified
     */
    public static final String FLAG_NOCOMMENT = "-nc";
        /**
     * -nwarn flag -- suppresses warning messages
     */
    public static final String FLAG_NOWARN = "-nwarn";
        /**
     * -ptime flag -- preserves the modification time
     */
    public static final String FLAG_PRESERVETIME = "-ptime";
        /**
     * -keep flag -- keeps a copy of the file with a .keep extension
     */
    public static final String FLAG_KEEPCOPY = "-keep";
        /**
     * -identical flag -- allows the file to be checked in even if it is identical to the original
     */
    public static final String FLAG_IDENTICAL = "-identical";

}

