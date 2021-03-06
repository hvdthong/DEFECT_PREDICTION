package org.apache.tools.ant.taskdefs.optional.vss;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Path;

import java.io.File;

/**
 * Performs CheckOut commands to Microsoft Visual SourceSafe.
 * <p>If you specify two or more attributes from version, date and 
 * label only one will be used in the order version, date, label.</p>
 * @author Martin Poeschl
 *
 * @ant.task name="vsscheckout" category="scm"
 */
public class MSVSSCHECKOUT extends MSVSS {

    private String m_LocalPath = null;
    private boolean m_Recursive = false;
    private String m_Version = null;
    private String m_Date = null;
    private String m_Label = null;
    private String m_AutoResponse = null;

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
        commandLine.createArgument().setValue(COMMAND_CHECKOUT);

        commandLine.createArgument().setValue(getVsspath());
        getLocalpathCommand(commandLine);
        getAutoresponse(commandLine);
        getRecursiveCommand(commandLine);
        getVersionCommand(commandLine);
        getLoginCommand(commandLine);

        result = run(commandLine);
        if (result != 0) {
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
     * Builds and returns the -GL flag command if required.
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
                if (!done) {
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
     * Flag to tell the task to recurse down the tree;
     * optional, default false.
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
     * Set the version to get;
     * optional, only one of <tt>version</tt>, <tt>label</tt>, or <tt>date</tt>
     * allowed.
     */
    public void setVersion(String version) {
        if (version.equals("") || version.equals("null")) {
            m_Version = null;
        } else {
            m_Version = version;
        }
    }

    /**
     * Set the date to get;
     * optional, only one of <tt>version</tt>, <tt>label</tt>, or <tt>date</tt>
     * allowed.
     */
    public void setDate(String date) {
        if (date.equals("") || date.equals("null")) {
            m_Date = null;
        } else {
            m_Date = date;
        }
    }

    /**
     * Set the label to get;
     * optional, only one of <tt>version</tt>, <tt>label</tt>, or <tt>date</tt>
     * allowed.
     */
    public void setLabel(String label) {
        if (label.equals("") || label.equals("null")) {
            m_Label = null;
        } else {
            m_Label = label;
        }
    }

    /**
     * Simple order of priority. Returns the first specified of version, date, label
     * If none of these was specified returns ""
     */
    public void getVersionCommand(Commandline cmd) {

        if (m_Version != null) {
            cmd.createArgument().setValue(FLAG_VERSION + m_Version);
        } else if (m_Date != null) {
            cmd.createArgument().setValue(FLAG_VERSION_DATE + m_Date);
        } else if (m_Label != null) {
            cmd.createArgument().setValue(FLAG_VERSION_LABEL + m_Label);
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

}

