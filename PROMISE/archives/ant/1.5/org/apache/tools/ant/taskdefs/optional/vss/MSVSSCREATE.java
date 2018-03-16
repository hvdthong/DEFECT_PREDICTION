package org.apache.tools.ant.taskdefs.optional.vss;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Commandline;

/**
 * Creates a new project in Microsoft Visual SourceSafe.
 * <p>
 * The following attributes are interpreted:
 * <table border="1">
 *   <tr>
 *     <th>Attribute</th>
 *     <th>Values</th>
 *     <th>Required</th>
 *   </tr>
 *   <tr>
 *      <td>login</td>
 *      <td>username,password</td>
 *      <td>No</td>
 *   </tr>
 *   <tr>
 *      <td>vsspath</td>
 *      <td>SourceSafe path of project to be created</td>
 *      <td>Yes</td>
 *   </tr>
 *   <tr>
 *      <td>ssdir</td>
 *      <td>directory where <code>ss.exe</code> resides. By default the task
 *      expects it to be in the PATH.</td>
 *      <td>No</td>
 *   </tr>
 *   <tr>
 *      <td>quiet</td>
 *      <td>suppress output (off by default)</td>
 *      <td>No</td>
 *   </tr>
 *   <tr>
 *      <td>failOnError</td>
 *      <td>fail if there is an error creating the project (true by default)</td>
 *      <td>No</td>
 *   </tr>   
 *   <tr>
 *      <td>autoresponse</td>
 *      <td>What to respond with (sets the -I option). By default, -I- is
 *      used; values of Y or N will be appended to this.</td>
 *      <td>No</td>
 *   </tr>
 *   <tr>
 *      <td>comment</td>
 *      <td>The comment to use for this label. Empty or '-' for no comment.</td>
 *      <td>No</td>
 *   </tr>
 *      
 * </table>
 *
 * @author Gary S. Weaver
 * @ant.task name="vsscreate" category="scm"
 */
public class MSVSSCREATE extends MSVSS {

    private String m_AutoResponse = null;
    private String m_Name = null;
    private String m_Comment = "-";
    private boolean m_Quiet = false;
  
    /**
     * True by default since most of the time we won't be trying to create a 
     * project with the same name more than once.
     */
    private boolean m_FailOnError = true;

    /**
     * Executes the task.
     * <p>
     * Builds a command line to execute ss and then calls Exec's run method
     * to execute the command line.
     * @throws BuildException if the task fails.
     */
    public void execute() throws BuildException {
        Commandline commandLine = new Commandline();
        int result = 0;

        if (getVsspath() == null) {
            String msg = "vsspath attribute must be set!";
            throw new BuildException(msg, location);
        }


        commandLine.setExecutable(getSSCommand());
        commandLine.createArgument().setValue(COMMAND_CREATE);

        commandLine.createArgument().setValue(getVsspath());

        commandLine.createArgument().setValue("-C" + getComment());

        getAutoresponse(commandLine);

        getQuietCommand(commandLine);

        getLoginCommand(commandLine);

        result = run(commandLine);
        if (result != 0 && m_FailOnError) {
            String msg = "Failed executing: " + commandLine.toString();
            throw new BuildException(msg, location);
        }

    }

    /**
     * Sets the comment to apply in SourceSafe.
     * <p>
     * If this is null or empty, it will be replaced with "-" which
     * is what SourceSafe uses for an empty comment.
     * @param comment the comment to apply in SourceSafe
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
     * Sets/clears quiet mode; optional, default false.
     * @param quiet whether or not command should be run in "quiet mode".
     */
    public final void setQuiet (boolean quiet) {
        this.m_Quiet = quiet;
    }

    /** 
     * Modify the commandline to add the quiet argument.
     * @param cmd the commandline to modify.
     */
    public void getQuietCommand (Commandline cmd) {
        if (m_Quiet) {
            cmd.createArgument().setValue (FLAG_QUIET);
        }
    }

    /**
     * Sets whether task should fail if there is an error creating the project;
     * optional, default true.
     * @param failOnError true if task should fail if there is an error creating 
     * the project.
     */
    public final void setFailOnError (boolean failOnError) {
        this.m_FailOnError = failOnError;
    }

    /**
     * What to respond with (sets the -I option). By default, -I- is
     * used; values of Y or N will be appended to this.
     * @param response the response.
     */
    public void setAutoresponse(String response) {
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
     * @param cmd the commandline to modify with the autoresponse.
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
}
