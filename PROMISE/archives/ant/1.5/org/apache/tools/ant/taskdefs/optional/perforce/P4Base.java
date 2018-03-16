package org.apache.tools.ant.taskdefs.optional.perforce;

import java.io.IOException;

import org.apache.oro.text.perl.Perl5Util;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.types.Commandline;


/** Base class for Perforce (P4) ANT tasks. See individual task for example usage.
 *
 * @see P4Sync
 * @see P4Have
 * @see P4Change
 * @see P4Edit
 * @see P4Submit
 * @see P4Label
 * @see org.apache.tools.ant.taskdefs.Exec
 * @author <A HREF="mailto:leslie.hughes@rubus.com">Les Hughes</A>
 */
public abstract class P4Base extends org.apache.tools.ant.Task {

    /**Perl5 regexp in Java - cool eh? */
    protected Perl5Util util = null;
    /** The OS shell to use (cmd.exe or /bin/sh) */
    protected String shell;

    /** Perforce Server Port (eg KM01:1666) */
    protected String P4Port = "";
    /** Perforce Client (eg myclientspec) */
    protected String P4Client = "";
    /** Perforce User (eg fbloggs) */
    protected String P4User = "";
    protected String P4View = "";

    /** Keep going or fail on error - defaults to fail. */
    protected boolean failOnError = true;

    /** Perforce 'global' opts.
     * Forms half of low level API */
    protected String P4Opts = "";
    /** Perforce command opts.
     * Forms half of low level API */
    protected String P4CmdOpts = "";

    
    /**
     * The p4d server and port to connect to;
     * optional, default "perforce:1666"
     */
    public void setPort(String P4Port) {
        this.P4Port = "-p" + P4Port;
    }

    /**
     * The p4 client spec to use;
     * optional, defaults to the current user
     */
    public void setClient(String P4Client) {
        this.P4Client = "-c" + P4Client;
    }

    /**
     * The p4 username;
     * optional, defaults to the current user
     */
    public void setUser(String P4User) {
        this.P4User = "-u" + P4User;
    }

    /**
     * The client, branch or label view to operate upon;
     */
    public void setView(String P4View) {
        this.P4View = P4View;
    }

    /**
     * Set extra command options; only used on some
     * of the Perforce tasks.
     */ 
    public void setCmdopts(String P4CmdOpts) {
        this.P4CmdOpts = P4CmdOpts;
    }

    /**
     * whether to stop the build (true, default) 
     * or keep going if an error is returned from the p4 command
     */
    public void setFailonerror(boolean fail) {
        failOnError = fail;
    }

    public void init() {

        util = new Perl5Util();

        String tmpprop;
        if ((tmpprop = project.getProperty("p4.port")) != null) {
            setPort(tmpprop);
        }
        if ((tmpprop = project.getProperty("p4.client")) != null) {
            setClient(tmpprop);
        }
        if ((tmpprop = project.getProperty("p4.user")) != null) {
            setUser(tmpprop);
        }
    }

    protected void execP4Command(String command) throws BuildException {
        execP4Command(command, null);
    }

    /** Execute P4 command assembled by subclasses.
     @param command The command to run
     @param p4input Input to be fed to command on stdin
     @param handler A P4Handler to process any input and output
     */
    protected void execP4Command(String command, P4Handler handler) throws BuildException {
        try {

            Commandline commandline = new Commandline();
            commandline.setExecutable("p4");

            if (P4Port != null && P4Port.length() != 0) {
                commandline.createArgument().setValue(P4Port);
            }
            if (P4User != null && P4User.length() != 0) {
                commandline.createArgument().setValue(P4User);
            }
            if (P4Client != null && P4Client.length() != 0) {
                commandline.createArgument().setValue(P4Client);
            }
            commandline.createArgument().setLine(command);


            String[] cmdline = commandline.getCommandline();
            String cmdl = "";
            for (int i = 0; i < cmdline.length; i++) {
                cmdl += cmdline[i] + " ";
            }

            log(commandline.describeCommand(), Project.MSG_VERBOSE);

            if (handler == null) {
                handler = new SimpleP4OutputHandler(this);
            }

            Execute exe = new Execute(handler, null);

            exe.setAntRun(project);

            exe.setCommandline(commandline.getCommandline());

            try {
                exe.execute();
            } catch (IOException e) {
                throw new BuildException(e);
            } finally {
                try {
                    handler.stop();
                } catch (Exception e) {
                }
            }


        } catch (Exception e) {
            String failMsg = "Problem exec'ing P4 command: " + e.getMessage();
            if (failOnError) {
                throw new BuildException(failMsg);
            } else {
                log(failMsg, Project.MSG_ERR);
            }

        }
    }
}
