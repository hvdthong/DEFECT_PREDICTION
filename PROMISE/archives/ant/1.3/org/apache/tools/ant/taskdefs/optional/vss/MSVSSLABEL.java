package org.apache.tools.ant.taskdefs.optional.vss;

import org.apache.tools.ant.*;
import org.apache.tools.ant.types.Commandline;

/**
 * Task to perform LABEL commands to Microsoft Visual Source Safe.
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
 * </table>
 *
 * @author Phillip Wells
 */
public class MSVSSLABEL extends MSVSS
{

    private String m_Label = null;
    private String m_Version = null;

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



        getLabelCommand(commandLine);

        getVersionCommand(commandLine);

        getLoginCommand(commandLine);

        result = run(commandLine);
        if ( result != 0 ) {
            String msg = "Failed executing: " + commandLine.toString();
            throw new BuildException(msg, location);
        }

    }

    /**
     * Set the label to apply in SourceSafe
     * <p>
     * Note we assume that if the supplied string has the value "null" that something
     * went wrong and that the string value got populated from a null object. This
     * happens if a ant variable is used e.g. label="${label_server}" when label_server
     * has not been defined to ant!
     */
    public void setLabel(String label) {
        if ( label.equals("") || label.equals("null") ) {
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
        if ( m_Version != null) {
            cmd.createArgument().setValue(FLAG_VERSION + m_Version);
        }
    }

    /**
     * Builds the label command.
     * @param cmd the commandline the command is to be added to
     */
    public void getLabelCommand(Commandline cmd) {
        if ( m_Label != null) {
            cmd.createArgument().setValue(FLAG_LABEL + m_Label);
        }
    }

    /**
     * Set the stored version string
     * <p>
     * Note we assume that if the supplied string has the value "null" that something
     * went wrong and that the string value got populated from a null object. This
     * happens if a ant variable is used e.g. version="${ver_server}" when ver_server
     * has not been defined to ant!
     */
    public void setVersion(String version) {
        if (version.equals("") || version.equals("null") ) {
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

}
