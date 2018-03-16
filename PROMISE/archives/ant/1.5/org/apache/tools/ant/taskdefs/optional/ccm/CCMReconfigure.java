package org.apache.tools.ant.taskdefs.optional.ccm;


import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Commandline;


/**
 * Task allows to reconfigure a project, recurcively or not
 * @author Benoit Moussaud benoit.moussaud@criltelecom.com
 */
public class CCMReconfigure extends Continuus {

    private String project = null;
    private boolean recurse = false;
    private boolean verbose = false;

    public CCMReconfigure() {
        super();
        setCcmAction(COMMAND_RECONFIGURE);
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
        if (result != 0) {
            String msg = "Failed executing: " + commandLine.toString();
            throw new BuildException(msg, location);
        }
    }


    /**
     * Check the command line options.
     */
    private void checkOptions(Commandline cmd) {

        if (isRecurse() == true) {
            cmd.createArgument().setValue(FLAG_RECURSE);

        if (isVerbose() == true) {
            cmd.createArgument().setValue(FLAG_VERBOSE);

        if (getCcmProject() != null) {
            cmd.createArgument().setValue(FLAG_PROJECT);
            cmd.createArgument().setValue(getCcmProject());
        }

    }

    /**
     * Get the value of project.
     * @return value of project.
     */
    public String getCcmProject() {
        return project;
    }

    /**
     * Sets the ccm project on which the operation is applied.
     * @param v  Value to assign to project.
     */
    public void setCcmProject(String v) {
        this.project = v;
    }


    /**
     * Get the value of recurse.
     * @return value of recurse.
     */
    public boolean isRecurse() {
        return recurse;
    }

    /**
     * If true, recurse on subproject (default false).
     *
     * @param v  Value to assign to recurse.
     */
    public void setRecurse(boolean v) {
        this.recurse = v;
    }


    /**
     * Get the value of verbose.
     * @return value of verbose.
     */
    public boolean isVerbose() {
        return verbose;
    }

    /**
     * If true, do a verbose reconfigure operation (default false).
     * @param v  Value to assign to verbose.
     */
    public void setVerbose(boolean v) {
        this.verbose = v;
    }


    /**
     * /recurse --
     */
    public static final String FLAG_RECURSE = "/recurse";

    /**
     * /recurse --
     */
    public static final String FLAG_VERBOSE = "/verbose";


    /**
     *  /project flag -- target project
     */
    public static final String FLAG_PROJECT = "/project";

}

