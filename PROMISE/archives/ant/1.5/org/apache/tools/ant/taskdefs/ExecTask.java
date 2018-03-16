package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Environment;
import org.apache.tools.ant.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.StringReader;
import java.io.FileNotFoundException;

/**
 * Executes a given command if the os platform is appropriate.
 *
 * @author duncan@x180.com
 * @author rubys@us.ibm.com
 * @author thomas.haas@softwired-inc.com
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 * @author <a href="mailto:mariusz@rakiura.org">Mariusz Nowostawski</a>
 *
 * @since Ant 1.2
 *
 * @ant.task category="control"
 */
public class ExecTask extends Task {

    private String os;
    private File out;
    private File dir;
    protected boolean failOnError = false;
    protected boolean newEnvironment = false;
    private Long timeout = null;
    private Environment env = new Environment();
    protected Commandline cmdl = new Commandline();
    private FileOutputStream fos = null;
    private ByteArrayOutputStream baos = null;
    private String outputprop;
    private String resultProperty;
    private boolean failIfExecFails = true;
    private boolean append = false;

    /** 
     * Controls whether the VM (1.3 and above) is used to execute the
     * command 
     */
    private boolean vmLauncher = true;

    /**
     * Timeout in milliseconds after which the process will be killed.
     *
     * @since Ant 1.5
     */
    public void setTimeout(Long value) {
        timeout = value;
    }

    /**
     * Timeout in milliseconds after which the process will be killed.
     */
    public void setTimeout(Integer value) {
        if (value == null) {
            timeout = null;
        } else {
            setTimeout(new Long(value.intValue()));
        }
    }

    /**
     * The command to execute.
     */
    public void setExecutable(String value) {
        cmdl.setExecutable(value);
    }

    /**
     * The working directory of the process.
     */
    public void setDir(File d) {
        this.dir = d;
    }

    /**
     * List of operating systems on which the command may be executed.
     */
    public void setOs(String os) {
        this.os = os;
    }

    /**
     * @ant.attribute ignore="true"
     */
    public void setCommand(Commandline cmdl) {
        log("The command attribute is deprecated. " +
            "Please use the executable attribute and nested arg elements.",
            Project.MSG_WARN);
        this.cmdl = cmdl;
    }

    /**
     * File the output of the process is redirected to.
     */
    public void setOutput(File out) {
        this.out = out;
    }

    /**
     * Property name whose value should be set to the output of
     * the process.
     */
    public void setOutputproperty(String outputprop) {
        this.outputprop = outputprop;
    }

    /**
     * Fail if the command exits with a non-zero return code.
     */
    public void setFailonerror(boolean fail) {
        failOnError = fail;
    }

    /**
     * Do not propagate old environment when new environment variables are specified.
     */
    public void setNewenvironment(boolean newenv) {
        newEnvironment = newenv;
    }

    /**
     * Add an environment variable to the launched process.
     */
    public void addEnv(Environment.Variable var) {
        env.addVariable(var);
    }

    /**
     * Adds a command-line argument.
     */
    public Commandline.Argument createArg() {
        return cmdl.createArgument();
    }

    /**
     * The name of a property in which the return code of the
     * command should be stored. Only of interest if failonerror=false.
     *
     * @since Ant 1.5
     */
    public void setResultProperty(String resultProperty) {
        this.resultProperty = resultProperty;
    }
    
    /**
     * helper method to set result property to the 
     * passed in value if appropriate
     */
    protected void maybeSetResultPropertyValue(int result) {
        String res = Integer.toString(result);
        if (resultProperty != null) {
            project.setNewProperty(resultProperty, res);
        }
    }
    
    /**
     * Stop the build if program cannot be started. Defaults to true.
     *
     * @since Ant 1.5     
     */
    public void setFailIfExecutionFails(boolean flag) {
        failIfExecFails = flag;
    }
    
    /**
     * Whether output should be appended to or overwrite an existing file.
     * Defaults to false.
     *
     * @since 1.30, Ant 1.5
     */
    public void setAppend(boolean append) {
        this.append = append;
    }

    /**
     * Do the work.
     */
    public void execute() throws BuildException {
        checkConfiguration();
        if (isValidOs()) {
            try {
                runExec(prepareExec());
            } finally {
                dir = savedDir;
            }
        }
    }

    /**
     * Has the user set all necessary attributes?
     */
    protected void checkConfiguration() throws BuildException {
        if (cmdl.getExecutable() == null) {
            throw new BuildException("no executable specified", location);
        }
        if (dir != null && !dir.exists()) {
            throw new BuildException("The directory you specified does not "
                                     + "exist");
        }
        if (dir != null && !dir.isDirectory()) {
            throw new BuildException("The directory you specified is not a "
                                     + "directory");
        }
    }

    /**
     * Is this the OS the user wanted?
     */
    protected boolean isValidOs() {
        String myos = System.getProperty("os.name");
        log("Current OS is " + myos, Project.MSG_VERBOSE);
        if ((os != null) && (os.indexOf(myos) < 0)){
            log("This OS, " + myos 
                + " was not found in the specified list of valid OSes: " + os,
                Project.MSG_VERBOSE);
            return false;
        }
        return true;
    }

    /**
     * If true, launch new process with VM, otherwise use the OS's shell.
     */
    public void setVMLauncher(boolean vmLauncher) {
        this.vmLauncher = vmLauncher;
    }

    /**
     * Create an Execute instance with the correct working directory set.
     */
    protected Execute prepareExec() throws BuildException {
        if (dir == null) {
            dir = project.getBaseDir();
        }
        Execute exe = new Execute(createHandler(), createWatchdog());
        exe.setAntRun(getProject());
        exe.setWorkingDirectory(dir);
        exe.setVMLauncher(vmLauncher);
        String[] environment = env.getVariables();
        if (environment != null) {
            for (int i = 0; i < environment.length; i++) {
                log("Setting environment variable: " + environment[i],
                    Project.MSG_VERBOSE);
            }
        }
        exe.setNewenvironment(newEnvironment);
        exe.setEnvironment(environment);
        return exe;
    }

    /**
     * A Utility method for this classes and subclasses to run an
     * Execute instance (an external command).
     */
    protected final void runExecute(Execute exe) throws IOException {

        err = exe.execute();
        if (exe.killedProcess()) {
            log("Timeout: killed the sub-process", Project.MSG_WARN); 
        }
        maybeSetResultPropertyValue(err);
        if (err != 0) {
            if (failOnError) {
                throw new BuildException(taskType + " returned: " + err,
                                         location);
            } else {
                log("Result: " + err, Project.MSG_ERR);
            }
        }
        if (baos != null) {
            BufferedReader in =
                new BufferedReader(new StringReader(Execute.toString(baos)));
            String line = null;
            StringBuffer val = new StringBuffer();
            while ((line = in.readLine()) != null) {
                if (val.length() != 0) {
                    val.append(StringUtils.LINE_SEP);
                }
                val.append(line);
            }
            project.setNewProperty(outputprop, val.toString());
        }
    }

    /**
     * Run the command using the given Execute instance. This may be
     * overidden by subclasses
     */
    protected void runExec(Execute exe) throws BuildException {
        log(cmdl.describeCommand(), Project.MSG_VERBOSE);

        exe.setCommandline(cmdl.getCommandline());
        try {
            runExecute(exe);
        } catch (IOException e) {
            if (failIfExecFails) {
                throw new BuildException("Execute failed: " + e.toString(), e,
                                         location);
            } else {
                log("Execute failed: " + e.toString(), Project.MSG_ERR);
            }
        } finally {
            logFlush();
        }
    }

    /**
     * Create the StreamHandler to use with our Execute instance.
     */
    protected ExecuteStreamHandler createHandler() throws BuildException {
        if (out != null)  {
            try {
                fos = new FileOutputStream(out.getAbsolutePath(), append);
                log("Output redirected to " + out, Project.MSG_VERBOSE);
                return new PumpStreamHandler(fos);
            } catch (FileNotFoundException fne) {
                throw new BuildException("Cannot write to " + out, fne, 
                                         location);
            } catch (IOException ioe) {
                throw new BuildException("Cannot write to " + out, ioe, 
                                         location);
            }
        } else if (outputprop != null) {
            baos = new ByteArrayOutputStream();
            log("Output redirected to ByteArray", Project.MSG_VERBOSE);
            return new PumpStreamHandler(baos);
        } else {
            return new LogStreamHandler(this,
                                        Project.MSG_INFO, Project.MSG_WARN);
        }
    }

    /**
     * Create the Watchdog to kill a runaway process.
     */
    protected ExecuteWatchdog createWatchdog() throws BuildException {
        if (timeout == null) {
            return null;
        }
        return new ExecuteWatchdog(timeout.longValue());
    }

    /**
     * Flush the output stream - if there is one.
     */
    protected void logFlush() {
        try {
            if (fos != null) {
                fos.close();
            }
            if (baos != null) {
                baos.close();
            }
        } catch (IOException io) {}
    }

}
