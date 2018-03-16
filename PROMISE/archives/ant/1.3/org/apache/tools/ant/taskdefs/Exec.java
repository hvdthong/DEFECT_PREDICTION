package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.*;
import java.io.*;

/**
 * Executes a given command if the os platform is appropriate.
 *
 * @author duncan@x180.com
 * @author rubys@us.ibm.com
 *
 * @deprected Instead of using this class, please extend ExecTask or
 * delegate to Execute.  
 */
public class Exec extends Task {
    private String os;
    private String out;
    private File dir;
    private String command;
    protected PrintWriter fos = null;
    private boolean failOnError = false;

    private static final int BUFFER_SIZE = 512;

    public void execute() throws BuildException {
        run(command);
    }

    protected int run(String command) throws BuildException {


        String myos = System.getProperty("os.name");
        log("Myos = " + myos, Project.MSG_VERBOSE);
        if ((os != null) && (os.indexOf(myos) < 0)){
            log("Not found in " + os, Project.MSG_VERBOSE);
            return 0;
        }

        if (dir == null) dir = project.getBaseDir();

        if (myos.toLowerCase().indexOf("windows") >= 0) {
            if (!dir.equals(project.resolveFile("."))) {
                if (myos.toLowerCase().indexOf("nt") >= 0) {
                    command = "cmd /c cd " + dir + " && " + command;
                }
                else {
                    String ant = project.getProperty("ant.home");
                    if (ant == null) {
                        throw new BuildException("Property 'ant.home' not found", location);
                    }
                
                    String antRun = project.resolveFile(ant + "/bin/antRun.bat").toString();
                    command = antRun + " " + dir + " " + command;
                }
            }
        } else {
            String ant = project.getProperty("ant.home");
            if (ant == null) throw new BuildException("Property 'ant.home' not found", location);
            String antRun = project.resolveFile(ant + "/bin/antRun").toString();

            command = antRun + " " + dir + " " + command;
        }

        try {
            log(command, Project.MSG_VERBOSE);

            Process proc = Runtime.getRuntime().exec(command);

            if( out!=null )  {
                fos=new PrintWriter( new FileWriter( out ) );
                log("Output redirected to " + out, Project.MSG_VERBOSE);
            }

            StreamPumper inputPumper =
                new StreamPumper(proc.getInputStream(), Project.MSG_INFO, this);
            StreamPumper errorPumper =
                new StreamPumper(proc.getErrorStream(), Project.MSG_WARN, this);

            inputPumper.start();
            errorPumper.start();

            proc.waitFor();
            inputPumper.join();
            errorPumper.join();
            proc.destroy();

            logFlush();

            err = proc.exitValue();
            if (err != 0) {
                if (failOnError) {
                    throw new BuildException("Exec returned: "+err, location);
                } else {
                    log("Result: " + err, Project.MSG_ERR);
                }
            }
        } catch (IOException ioe) {
            throw new BuildException("Error exec: " + command, ioe, location);
        } catch (InterruptedException ex) {}

        return err;
    }

    public void setDir(String d) {
        this.dir = project.resolveFile(d);
    }

    public void setOs(String os) {
        this.os = os;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setOutput(String out) {
        this.out = out;
    }

    public void setFailonerror(boolean fail) {
        failOnError = fail;
    }

    protected void outputLog(String line, int messageLevel) {
        if (fos == null) {
            log(line, messageLevel); 
        } else {
            fos.println(line);
        }
    };

    protected void logFlush() {
        if (fos != null) fos.close();
    }

    class StreamPumper extends Thread {
        private BufferedReader din;
        private int messageLevel;
        private boolean endOfStream = false;
        private int SLEEP_TIME = 5;
        private Exec parent;

        public StreamPumper(InputStream is, int messageLevel, Exec parent) {
            this.din = new BufferedReader(new InputStreamReader(is));
            this.messageLevel = messageLevel;
            this.parent = parent;
        }

        public void pumpStream()
            throws IOException
        {
            byte[] buf = new byte[BUFFER_SIZE];
            if (!endOfStream) {
                String line = din.readLine();

                if (line != null) {
                    outputLog(line, messageLevel);
                } else {
                    endOfStream = true;
                }
            }
        }

        public void run() {
            try {
                try {
                    while (!endOfStream) {
                        pumpStream();
                        sleep(SLEEP_TIME);
                    }
                } catch (InterruptedException ie) {}
                din.close();
            } catch (IOException ioe) {}
        }
    }
}
