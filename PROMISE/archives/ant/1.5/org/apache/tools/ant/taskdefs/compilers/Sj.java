package org.apache.tools.ant.taskdefs.compilers;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Commandline;

/**
 * The implementation of the sj compiler.
 * Uses the defaults for DefaultCompilerAdapter
 * 
 * @author <a href="mailto:don@bea.com">Don Ferguson</a>
 * @since Ant 1.4
 */
public class Sj extends DefaultCompilerAdapter {

    /**
     * Performs a compile using the sj compiler from Symantec.
     */
    public boolean execute() throws BuildException {
        attributes.log("Using symantec java compiler", Project.MSG_VERBOSE);

        Commandline cmd = setupJavacCommand();
        cmd.setExecutable("sj");

        int firstFileName = cmd.size() - compileList.length;

        return 
            executeExternalCompile(cmd.getCommandline(), firstFileName) == 0;
    }


}

