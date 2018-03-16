package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.util.FileUtils;

/**
 * Encapsulates a Jikes compiler, by directly executing an external
 * process.
 *
 * <p><strong>As of Ant 1.2, this class is considered to be dead code
 * by the Ant developers and is unmaintained.  Don't use
 * it.</strong></p>
 *
 * @deprecated since 1.2.
 *             Merged into the class Javac.
 */
public class Jikes {
    private static final int MAX_FILES_ON_COMMAND_LINE = 250;
    protected JikesOutputParser jop;
    protected String command;
    protected Project project;

    /**
     * Constructs a new Jikes object.
     * @param jop      Parser to send jike's output to
     * @param command  name of jikes executable
     * @param project  the current project
     */
    protected Jikes(JikesOutputParser jop, String command, Project project) {
        super();

        System.err.println("As of Ant 1.2 released in October 2000, "
            + "the Jikes class");
        System.err.println("is considered to be dead code by the Ant "
            + "developers and is unmaintained.");
        System.err.println("Don\'t use it!");

        this.jop = jop;
        this.command = command;
        this.project = project;
    }

    /**
     * Do the compile with the specified arguments.
     * @param args - arguments to pass to process on command line
     */
    protected void compile(String[] args) {
        String[] commandArray = null;
        File tmpFile = null;

        try {
            String myos = System.getProperty("os.name");


            if (myos.toLowerCase().indexOf("windows") >= 0
                && args.length > MAX_FILES_ON_COMMAND_LINE) {
                PrintWriter out = null;
                try {
                    String tempFileName = "jikes"
                        + (new Random(System.currentTimeMillis())).nextLong();
                    tmpFile = new File(tempFileName);
                    out = new PrintWriter(new FileWriter(tmpFile));
                    for (int i = 0; i < args.length; i++) {
                        out.println(args[i]);
                    }
                    out.flush();
                    commandArray = new String[] {command,
                                               "@" + tmpFile.getAbsolutePath()};
                } catch (IOException e) {
                    throw new BuildException("Error creating temporary file",
                                             e);
                } finally {
                    FileUtils.close(out);
                }
            } else {
                commandArray = new String[args.length + 1];
                commandArray[0] = command;
                System.arraycopy(args, 0, commandArray, 1, args.length);
            }

            try {
                Execute exe = new Execute(jop);
                exe.setAntRun(project);
                exe.setWorkingDirectory(project.getBaseDir());
                exe.setCommandline(commandArray);
                exe.execute();
            } catch (IOException e) {
                throw new BuildException("Error running Jikes compiler", e);
            }
        } finally {
            if (tmpFile != null) {
                tmpFile.delete();
            }
        }
    }
}
