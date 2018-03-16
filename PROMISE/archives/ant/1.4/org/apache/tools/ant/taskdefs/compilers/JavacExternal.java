package org.apache.tools.ant.taskdefs.compilers;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.*;

/**
 * Performs a compile using javac externally.
 *
 * @author Brian Deitte
 */
public class JavacExternal extends DefaultCompilerAdapter {

    /**
     * Performs a compile using the Javac externally.
     */
    public boolean execute() throws BuildException {
        attributes.log("Using external javac compiler", Project.MSG_VERBOSE);

        Commandline cmd = new Commandline();
        cmd.setExecutable("javac");
        setupJavacCommandlineSwitches(cmd);
        int firstFileName = cmd.size();
        logAndAddFilesToCompile(cmd);

        return executeExternalCompile(cmd.getCommandline(), firstFileName) == 0;
    }
}

