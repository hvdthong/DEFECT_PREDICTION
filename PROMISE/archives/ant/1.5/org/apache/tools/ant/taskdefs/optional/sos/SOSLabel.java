package org.apache.tools.ant.taskdefs.optional.sos;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Commandline;

/**
 * Labels Visual SourceSafe files via a SourceOffSite server.
 * <p>
 * The following attributes are interpreted:
 * <table border="1">
 *   <tr>
 *     <th>Attribute</th>
 *     <th>Values</th>
 *     <th>Required</th>
 *   </tr>
 *   <tr>
 *     <td>soscmddir</td>
 *     <td>Directory which contains soscmd(.exe) <br>
 *     soscmd(.exe) must be in the path if this is not specified</td>
 *     <td>No</td>
 *   </tr>
 *   <tr>
 *      <td>vssserverpath</td>
 *      <td>path to the srcsafe.ini  - eg. \\server\vss\srcsafe.ini</td>
 *      <td>Yes</td>
 *   </tr>
 *   <tr>
 *      <td>sosserverpath</td>
 *      <td>address and port of the SOS server  - eg. 192.168.0.1:8888</td>
 *      <td>Yes</td>
 *   </tr>
 *   <tr>
 *      <td>projectpath</td>
 *      <td>SourceSafe project path without the "$"</td>
 *      <td>Yes</td>
 *   </tr>
 *   <tr>
 *      <td>username</td>
 *      <td>SourceSafe username</td>
 *      <td>Yes</td>
 *   </tr>
 *   <tr>
 *      <td>password</td>
 *      <td>SourceSafe password</td>
 *      <td>No</td>
 *   </tr>
 *   <tr>
 *      <td>label</td>
 *      <td>The label to apply to a project</td>
 *      <td>Yes</td>
 *   </tr>
 *   <tr>
 *      <td>comment</td>
 *      <td>A comment to be applied to all files being labeled</td>
 *      <td>No</td>
 *   </tr>
 *   <tr>
 *      <td>verbose</td>
 *      <td>true or false - Status messages are displayed</td>
 *      <td>No</td>
 *   </tr>
 * </table>
 *
 * @author    <a href="mailto:jesse@cryptocard.com">Jesse Stockall</a>
 */

public class SOSLabel extends SOS {
    Commandline commandLine;


    /**
     *  Executes the task. <br>
     *  Builds a command line to execute soscmd and then calls Exec's run method
     *  to execute the command line.
     *
     * @exception  BuildException  Description of Exception
     */
    public void execute() throws BuildException {
        int result = 0;
        buildCmdLine();
        result = run(commandLine);
        if (result == 255) {
            String msg = "Failed executing: " + commandLine.toString();
            throw new BuildException(msg, location);
        }
    }


    /**
     *  Build the command line <br>
     *  AddLabel required parameters: -server -name -password -database -project -label<br>
     *  AddLabel optional parameters: -verbose -comment<br>
     *
     * @return    Commandline the generated command to be executed
     */
    protected Commandline buildCmdLine() {
        commandLine = new Commandline();
        commandLine.setExecutable(getSosCommand());
        commandLine.createArgument().setValue(SOSCmd.FLAG_COMMAND);
        commandLine.createArgument().setValue(SOSCmd.COMMAND_LABEL);
        if (getSosServerPath() == null) {
            throw new BuildException("sosserverpath attribute must be set!", location);
        }
        commandLine.createArgument().setValue(SOSCmd.FLAG_SOS_SERVER);
        commandLine.createArgument().setValue(getSosServerPath());
        if (getUsername() == null) {
            throw new BuildException("username attribute must be set!", location);
        }
        commandLine.createArgument().setValue(SOSCmd.FLAG_USERNAME);
        commandLine.createArgument().setValue(getUsername());
        commandLine.createArgument().setValue(SOSCmd.FLAG_PASSWORD);
        commandLine.createArgument().setValue(getPassword());
        if (getVssServerPath() == null) {
            throw new BuildException("vssserverpath attribute must be set!", location);
        }
        commandLine.createArgument().setValue(SOSCmd.FLAG_VSS_SERVER);
        commandLine.createArgument().setValue(getVssServerPath());
        if (getProjectPath() == null) {
            throw new BuildException("projectpath attribute must be set!", location);
        }
        commandLine.createArgument().setValue(SOSCmd.FLAG_PROJECT);
        commandLine.createArgument().setValue(getProjectPath());
        if (getLabel() == null) {
            throw new BuildException("label attribute must be set!", location);
        }
        commandLine.createArgument().setValue(SOSCmd.FLAG_LABEL);
        commandLine.createArgument().setValue(getLabel());


        commandLine.createArgument().setValue(getVerbose());
        if (getComment() != null) {
            commandLine.createArgument().setValue(SOSCmd.FLAG_COMMENT);
            commandLine.createArgument().setValue(getComment());
        }
        return commandLine;
    }
}

