package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.*;
import org.apache.tools.ant.types.*;

import java.io.*;

/**
 * Executes a given command if the os platform is appropriate.
 *
 * @author duncan@x180.com
 * @author rubys@us.ibm.com
 * @author thomas.haas@softwired-inc.com
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 * @author <a href="mailto:mariusz@rakiura.org">Mariusz Nowostawski</a> 
 */
public class ExecTask extends Task {

    private static String lSep = System.getProperty("line.separator");

    private String os;
    private File out;
    private File dir;
    protected boolean failOnError = false;
    protected boolean newEnvironment = false;
    private Integer timeout = null;
    private Environment env = new Environment();
    protected Commandline cmdl = new Commandline();
    private FileOutputStream fos = null;
    private ByteArrayOutputStream baos = null;
    private String outputprop;

    /** Controls whether the VM (1.3 and above) is used to execute the command */
    private boolean vmLauncher = true;
     
    /**
     * Timeout in milliseconds after which the process will be killed.
     */
    public void setTimeout(Integer value) {
        timeout = value;
    }

    /**
     * The command to execute.
     */
    public void setExecutable(String value) {
        cmdl.setExecutable(value);
    }

    /**
     * The working directory of the process
     */
    public void setDir(File d) {
        this.dir = d;
    }

    /**
     * Only execute the process if <code>os.name</code> is included in this string.
     */
    public void setOs(String os) {
        this.os = os;
    }

    /**
     * The full commandline to execute, executable + arguments.
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
     * the process
     */
    public void setOutputproperty(String outputprop) {
	this.outputprop = outputprop;
    }

    /**
     * Throw a BuildException if process returns non 0.
     */
    public void setFailonerror(boolean fail) {
        failOnError = fail;
    }

    /**
     * Use a completely new environment
     */
    public void setNewenvironment(boolean newenv) {
        newEnvironment = newenv;
    }

    /**
     * Add a nested env element - an environment variable.
     */
    public void addEnv(Environment.Variable var) {
        env.addVariable(var);
    }

    /**
     * Add a nested arg element - a command line argument.
     */
    public Commandline.Argument createArg() {
        return cmdl.createArgument();
    }

    /**
     * Do the work.
     */
    public void execute() throws BuildException {
        checkConfiguration();
        if (isValidOs()) {
            runExec(prepareExec());
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
        	throw new BuildException("The directory you specified does not exist");
        }
        if (dir != null && !dir.isDirectory()) {
        	throw new BuildException("The directory you specified is not a directory");
        }
    }

    /**
     * Is this the OS the user wanted?
     */
    protected boolean isValidOs() {
        String myos = System.getProperty("os.name");
        log("Current OS is " + myos, Project.MSG_VERBOSE);
        if ((os != null) && (os.indexOf(myos) < 0)){
            log("This OS, " + myos + " was not found in the specified list of valid OSes: " + os, Project.MSG_VERBOSE);
            return false;
        }
        return true;
    }

    /**
     * Control whether the VM is used to launch the new process or
     * whether the OS's shell is used.
     */
    public void setVMLauncher(boolean vmLauncher) {
        this.vmLauncher = vmLauncher;
    }
    
    /**
     * Create an Execute instance with the correct working directory set.
     */
    protected Execute prepareExec() throws BuildException {
        if (dir == null) dir = project.getBaseDir();
        log(cmdl.toString(), Project.MSG_VERBOSE);
        
        Execute exe = new Execute(createHandler(), createWatchdog());
        exe.setAntRun(project);
        exe.setWorkingDirectory(dir);
        exe.setVMLauncher(vmLauncher);
        String[] environment = env.getVariables();
        if (environment != null) {
            for (int i=0; i<environment.length; i++) {
                log("Setting environment variable: "+environment[i],
                    Project.MSG_VERBOSE);
            }
        }
        exe.setNewenvironment(newEnvironment);
        exe.setEnvironment(environment);
        return exe;
    }

    /**
     * A Utility method for this classes and subclasses to run an Execute instance (an external command).
     */
    protected final void runExecute(Execute exe) throws IOException {

        err = exe.execute();
        if (err != 0) {
            if (failOnError) {
                throw new BuildException(taskType + " returned: "+err, location);
            } else {
                log("Result: " + err, Project.MSG_ERR);
            }
        }
        if (baos != null) {
            BufferedReader in = 
                new BufferedReader(new StringReader(baos.toString()));
            String line = null;
            StringBuffer val = new StringBuffer();
            while ((line = in.readLine()) != null) {
                if (val.length() != 0) {
                    val.append(lSep);
                }
                val.append(line);
            }
            project.setProperty(outputprop, val.toString());
        }
    }
    
    /**
     * Run the command using the given Execute instance. This may be overidden by subclasses
     */
    protected void runExec(Execute exe) throws BuildException {
        exe.setCommandline(cmdl.getCommandline());
        try {
            runExecute(exe);
        } catch (IOException e) {
            throw new BuildException("Execute failed: " + e, e, location);
        } finally {
            logFlush();
        }
    }

    /**
     * Create the StreamHandler to use with our Execute instance.
     */
    protected ExecuteStreamHandler createHandler() throws BuildException {
        if(out!=null)  {
            try {
                fos = new FileOutputStream(out);
                log("Output redirected to " + out, Project.MSG_VERBOSE);
                return new PumpStreamHandler(fos);
            } catch (FileNotFoundException fne) {
                throw new BuildException("Cannot write to "+out, fne, location);
            } catch (IOException ioe) {
                throw new BuildException("Cannot write to "+out, ioe, location);
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
        if (timeout == null) return null;
        return new ExecuteWatchdog(timeout.intValue());
    }

    /**
     * Flush the output stream - if there is one.
     */
    protected void logFlush() {
        try {
            if (fos != null) fos.close();
            if (baos != null) baos.close();
        } catch (IOException io) {}
    }

}
