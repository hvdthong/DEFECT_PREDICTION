package org.apache.tools.ant.taskdefs.optional;

import java.io.*;
import org.apache.tools.ant.*;
import org.apache.tools.ant.taskdefs.*;
import org.apache.tools.ant.types.Commandline;

/**
 *
 *  @author lucas@collab.net
 */
public class Rpm extends Task {
    
    /**
     * the spec file
     */
    private String specFile;

    /**
     * the rpm top dir
     */
    private File topDir;

    /**
     * the rpm command to use
     */
    private String command = "-bb";

    /**
     * clean BUILD directory
     */
    private boolean cleanBuildDir = false;

    /**
     * remove spec file
     */
    private boolean removeSpec = false;

    /**
     * remove sources
     */
    private boolean removeSource = false;

    /**
     * the file to direct standard output from the command
     */
    private File output;

    /**
     * the file to direct standard error from the command
     */
    private File error;

    public void execute() throws BuildException {
        
        Commandline toExecute = new Commandline();

        toExecute.setExecutable("rpm");
        if (topDir != null) {
            toExecute.createArgument().setValue("--define");
            toExecute.createArgument().setValue("_topdir" + topDir);
        }

        toExecute.createArgument().setLine(command);

        if (cleanBuildDir) {
            toExecute.createArgument().setValue("--clean");
        }
        if (removeSpec) {
            toExecute.createArgument().setValue("--rmspec");
        }
        if (removeSource) {
            toExecute.createArgument().setValue("--rmsource");
        }

        toExecute.createArgument().setValue("SPECS/" + specFile);

        ExecuteStreamHandler streamhandler = null;
        OutputStream outputstream = null;
        OutputStream errorstream = null;
        if (error == null && output == null) {
            streamhandler = new LogStreamHandler(this, Project.MSG_INFO,
                                                 Project.MSG_WARN);
        }
        else {
            if (output != null) {
                try {
                    outputstream = new PrintStream(new BufferedOutputStream(new FileOutputStream(output)));
                } catch (IOException e) {
                    throw new BuildException(e,location);
                }
            }
            else {
                outputstream = new LogOutputStream(this,Project.MSG_INFO);
            }
            if (error != null) {
                try {
                    errorstream = new PrintStream(new BufferedOutputStream(new FileOutputStream(error)));
                }  catch (IOException e) {
                    throw new BuildException(e,location);
                }
            }
            else {
                errorstream = new LogOutputStream(this, Project.MSG_WARN);
            }
            streamhandler = new PumpStreamHandler(outputstream, errorstream);
        }

        Execute exe = new Execute(streamhandler, null);

        exe.setAntRun(project);
        if (topDir == null) topDir = project.getBaseDir();
        exe.setWorkingDirectory(topDir);

        exe.setCommandline(toExecute.getCommandline());
        try {
            exe.execute();
            log("Building the RPM based on the " + specFile + " file");
        } catch (IOException e) {
            throw new BuildException(e, location);
        } finally {
            if (output != null) {
                try {
                    outputstream.close();
                } catch (IOException e) {}
            }
            if (error != null) {
                try {
                    errorstream.close();
                } catch (IOException e) {}
            }
        }
    }

    public void setTopDir(File td) {
        this.topDir = td;
    }

    public void setCommand(String c) {
        this.command = c;
    }

    public void setSpecFile(String sf) {
        if ( (sf == null) || (sf.trim().equals(""))) {
            throw new BuildException("You must specify a spec file",location);
        }
        this.specFile = sf;
    }

    public void setCleanBuildDir(boolean cbd) {
        cleanBuildDir = cbd;
    }

    public void setRemoveSpec(boolean rs) {
        removeSpec = rs;
    }

    public void setRemoveSource(boolean rs) {
        removeSource = rs;
    }

    public void setOutput(File output) {
        this.output = output;
    }

    public void setError(File error) {
        this.error = error;
    }
}
