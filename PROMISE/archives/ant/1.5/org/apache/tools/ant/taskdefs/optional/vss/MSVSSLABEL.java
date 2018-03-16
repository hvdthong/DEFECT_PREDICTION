package org.apache.tools.ant.taskdefs.optional.vss;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Commandline;

/**
 * Performs Label commands to Microsoft Visual SourceSafe.
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
 *      <td>login</td>
 *      <td>username,password</td>
 *      <td>No</td>
 *   </tr>
 *   <tr>
 *      <td>vsspath</td>
 *      <td>SourceSafe path</td>
 *      <td>Yes</td>
 *   </tr>
 *   <tr>
 *      <td>ssdir</td>
 *      <td>directory where <code>ss.exe</code> resides. By default the task
 *      expects it to be in the PATH.</td>
 *      <td>No</td>
 *   </tr>
 *   <tr>
 *      <td>label</td>
 *      <td>A label to apply to the hierarchy</td>
 *      <td>Yes</td>
 *   </tr>
 *   <tr>
 *      <td>version</td>
 *      <td>An existing file or project version to label</td>
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
 * @author Phillip Wells
 *
 * @ant.task name="vsslabel" category="scm"
 */
public class MSVSSLABEL extends MSVSS {
    private String m_AutoResponse = null;
    private String m_Label = null;
    private String m_Version = null;
    private String m_Comment = "-";

    public static final String FLAG_LABEL = "-L";

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
        if (getLabel() == null) {
            String msg = "label attribute must be set!";
            throw new BuildException(msg, location);
        }


        commandLine.setExecutable(getSSCommand());
        commandLine.createArgument().setValue(COMMAND_LABEL);

        commandLine.createArgument().setValue(getVsspath());

        commandLine.createArgument().setValue("-C" + getComment());

        getAutoresponse(commandLine);

        getLabelCommand(commandLine);

        getVersionCommand(commandLine);

        getLoginCommand(commandLine);

        result = run(commandLine);
        if (result != 0) {
            String msg = "Failed executing: " + commandLine.toString();
            throw new BuildException(msg, location);
        }

    }

    /**
     * Set the label to apply; required.
     * <p>
     * Note we assume that if the supplied string has the value "null" that something
     * went wrong and that the string value got populated from a null object. This
     * happens if a ant variable is used e.g. label="${label_server}" when label_server
     * has not been defined to ant!
     * @todo correct. 
     */
    public void setLabel(String label) {
        if (label.equals("") || label.equals("null")) {
            m_Label = null;
        } else {
            m_Label = label;
        }
    }

    /**
     * Builds the version command.
     * @param cmd the commandline the command is to be added to
     */
    public void getVersionCommand(Commandline cmd) {
        if (m_Version != null) {
            cmd.createArgument().setValue(FLAG_VERSION + m_Version);
        }
    }

    /**
     * Builds the label command.
     * @param cmd the commandline the command is to be added to
     */
    public void getLabelCommand(Commandline cmd) {
        if (m_Label != null) {
            cmd.createArgument().setValue(FLAG_LABEL + m_Label);
        }
    }

    /**
     * Name an existing file or project version to label; optional.
     * By default the current version is labelled.
     * <p>
     * Note we assume that if the supplied string has the value "null" that something
     * went wrong and that the string value got populated from a null object. This
     * happens if a ant variable is used e.g. version="${ver_server}" when ver_server
     * has not been defined to ant!
     * @todo fix
     */
    public void setVersion(String version) {
        if (version.equals("") || version.equals("null")) {
            m_Version = null;
        } else {
            m_Version = version;
        }
    }

    /**
     * Gets the label to be applied.
     * @return the label to be applied.
     */
    public String getLabel() {
        return m_Label;
    }

    /**
     * The comment to use for this label; optional.
     * Empty or '-' for no comment.
     * <p>
     * If this is null or empty, it will be replaced with "-" which
     * is what SourceSafe uses for an empty comment.
      *@todo correct
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
}
