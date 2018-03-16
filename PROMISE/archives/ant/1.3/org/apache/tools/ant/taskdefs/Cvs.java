package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.*;
import org.apache.tools.ant.types.Commandline;
import java.io.*;

/**
 *
 *
 * @author costin@dnt.ro
 * @author stefano@apache.org
 * @author Wolfgang Werner <a href="mailto:wwerner@picturesafe.de">wwerner@picturesafe.de</a>
 */

public class Cvs extends Task {

    private Commandline cmd = new Commandline();
    
    /**
     * the CVSROOT variable.
     */
    private String cvsRoot;

    /**
     * the package/module to check out.
     */
    private String pack;

    /**
     * the CVS command to execute.
     */
    private String command = "checkout";

    /**
     * suppress information messages.
     */
    private boolean quiet = false;

    /**
     * report only, don't change any files.
     */
    private boolean noexec = false;

    /**
     * the directory where the checked out files should be placed.
     */
    private File dest;

    /**
     * the file to direct standard output from the command.
     */
    private File output;

    /**
     * the file to direct standard error from the command.
     */
    private File error; 

    public void execute() throws BuildException {


    
        Commandline toExecute = new Commandline();

        toExecute.setExecutable("cvs");
        if (cvsRoot != null) { 
            toExecute.createArgument().setValue("-d");
            toExecute.createArgument().setValue(cvsRoot);
        }
        if (noexec) {
            toExecute.createArgument().setValue("-n");
        }
        if (quiet) {
            toExecute.createArgument().setValue("-q");
        }
        toExecute.createArgument().setLine(command);
        toExecute.addArguments(cmd.getCommandline());

        if (pack != null) {
            toExecute.createArgument().setLine(pack);
        }

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
                    throw new BuildException(e, location);
                }
            }
            else {
                outputstream = new LogOutputStream(this, Project.MSG_INFO);
            }
            if (error != null) {
                try {
                    errorstream = new PrintStream(new BufferedOutputStream(new FileOutputStream(error)));
                } catch (IOException e) {
                    throw new BuildException(e, location);
                }
            }
            else {
                errorstream = new LogOutputStream(this, Project.MSG_WARN);
            }
            streamhandler = new PumpStreamHandler(outputstream, errorstream);
        }

        Execute exe = new Execute(streamhandler, 
                                  null);

        exe.setAntRun(project);
        if (dest == null) dest = project.getBaseDir();
        exe.setWorkingDirectory(dest);

        exe.setCommandline(toExecute.getCommandline());
        try {
            exe.execute();
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

    public void setCvsRoot(String root) {
        if (root != null) { 
            if (root.trim().equals("")) 
                root = null; 
        } 

        this.cvsRoot = root;
    }

    public void setDest(File dest) {
        this.dest = dest;
    }

    public void setPackage(String p) {
        this.pack = p;
    }

    public void setTag(String p) { 
        if (p != null && p.trim().length() > 0) {
            cmd.createArgument().setValue("-r");
            cmd.createArgument().setValue(p);
        }
    } 

    
    public void setDate(String p) {
        if(p != null && p.trim().length() > 0) {
            cmd.createArgument().setValue("-D");
            cmd.createArgument().setValue(p);
        }
    }

    public void setCommand(String c) {
        this.command = c;
    }
    
    public void setQuiet(boolean q) {
        quiet = q;
    }
    
    public void setNoexec(boolean ne) {
        noexec = ne;
    }

    public void setOutput(File output) {
        this.output = output;
    }
    
    public void setError(File error) {
        this.error = error;
    }
}


