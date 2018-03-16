package org.apache.tools.ant.taskdefs.compilers;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Commandline;

/**
 * Performs a compile using javac externally.
 *
 * @author Brian Deitte
 * @since Ant 1.4
 */
public class JavacExternal extends DefaultCompilerAdapter {

    /**
     * Performs a compile using the Javac externally.
     */
    public boolean execute() throws BuildException {
        attributes.log("Using external javac compiler", Project.MSG_VERBOSE);

        Commandline cmd = new Commandline();
        cmd.setExecutable(getJavac().getJavacExecutable());
        setupModernJavacCommandlineSwitches(cmd);
        int firstFileName = assumeJava11() ? -1 : cmd.size();
        logAndAddFilesToCompile(cmd);

        return 
            executeExternalCompile(cmd.getCommandline(), firstFileName) == 0;
    }

}

