package org.apache.tools.ant.taskdefs.optional;

import java.io.*;
import org.apache.tools.ant.*;
import org.apache.tools.ant.taskdefs.*;
import org.apache.tools.ant.types.*;
/**
 * @author Erik Meade, emeade@geekfarm.org
 */
public class ANTLR extends Task {

    private CommandlineJava commandline = new CommandlineJava();
    private File target;
    private File outputDirectory;
    private boolean fork = false;
    private File dir;

    public ANTLR() {
        commandline.setVm("java");
        commandline.setClassname("antlr.Tool");
    }

    public void setTarget(File target) {
        log("Setting target to: " + target.toString(), Project.MSG_VERBOSE);
        this.target = target;
    }

    public void setOutputdirectory(File outputDirectory) {
        log("Setting output directory to: " + outputDirectory.toString(), Project.MSG_VERBOSE);
        this.outputDirectory = outputDirectory;
    }

    public void setFork(boolean s) {
        this.fork = s;
    }

    /**
     * The working directory of the process
     */
    public void setDir(File d) {
        this.dir = d;
    }


    public void execute() throws BuildException {
        validateAttributes();

        if (target.lastModified() > getGeneratedFile().lastModified()) {
            commandline.createArgument().setValue("-o");
            commandline.createArgument().setValue(outputDirectory.toString());
            commandline.createArgument().setValue(target.toString());

            if (fork) {
                log("Forking " + commandline.toString(), Project.MSG_VERBOSE);
                int err = run(commandline.getCommandline());
                if (err == 1) {
                    throw new BuildException("ANTLR returned: "+err, location);
                }
            }
            else {
                Execute.runCommand(this, commandline.getCommandline());
            }
        }
    }

    private void validateAttributes() throws BuildException{
        if (target == null || !target.isFile()) {
            throw new BuildException("Invalid target: " + target);
        }

        if (outputDirectory == null) {
            String fileName = target.toString();
            setOutputdirectory(new File(target.getParent()));
        }
        if (!outputDirectory.isDirectory()) {
            throw new BuildException("Invalid output directory: " + outputDirectory);
        }
        if (fork && (dir == null || !dir.isDirectory())) {
            throw new BuildException("Invalid working directory: " + dir);
        }
    }

    private File getGeneratedFile() throws BuildException {
        String generatedFileName = null;
        try {
            BufferedReader in = new BufferedReader(new FileReader(target));
            String line;
            while ((line = in.readLine()) != null) {
                int extendsIndex = line.indexOf(" extends ");
                if (line.startsWith("class ") &&  extendsIndex > -1) {
                    generatedFileName = line.substring(6, extendsIndex).trim();
                    break;
                }
            }
            in.close();
        } catch (Exception e) {
            throw new BuildException("Unable to determine generated class");
        }
        if (generatedFileName == null) {
            throw new BuildException("Unable to determine generated class");
        }
        return new File(outputDirectory, generatedFileName + ".java");
    }

    private int run(String[] command) throws BuildException {
        Execute exe = new Execute(new LogStreamHandler(this, Project.MSG_INFO,
                                                       Project.MSG_WARN), null);
        exe.setAntRun(project);
        exe.setWorkingDirectory(dir);
        exe.setCommandline(command);
        try {
            return exe.execute();
        } catch (IOException e) {
            throw new BuildException(e, location);
        }
    }
}
