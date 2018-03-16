package org.apache.tools.ant.taskdefs.compilers;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Commandline;

/**
 * The implementation of the jvc compiler from microsoft.
 * This is primarily a cut-and-paste from the original javac task before it
 * was refactored.
 *
 * @author James Davidson <a href="mailto:duncan@x180.com">duncan@x180.com</a>
 * @author Robin Green 
 *         <a href="mailto:greenrd@hotmail.com">greenrd@hotmail.com</a>
 * @author Stefan Bodewig 
 * @author <a href="mailto:jayglanville@home.com">J D Glanville</a>
 * @since Ant 1.3
 */
public class Jvc extends DefaultCompilerAdapter {

    /**
     * Run the compilation.
     *
     * @exception BuildException if the compilation has problems.
     */
    public boolean execute() throws BuildException {
        attributes.log("Using jvc compiler", Project.MSG_VERBOSE);

        Path classpath = new Path(project);

        if (bootclasspath != null) {
            classpath.append(bootclasspath);
        }

        classpath.addExtdirs(extdirs);

        classpath.append(getCompileClasspath());

        if (compileSourcepath != null) {
            classpath.append(compileSourcepath);
        } else {
            classpath.append(src);
        }

        Commandline cmd = new Commandline();
        cmd.setExecutable("jvc");

        if (destDir != null) {
            cmd.createArgument().setValue("/d");
            cmd.createArgument().setFile(destDir);
        }
        
        cmd.createArgument().setValue("/cp:p");
        cmd.createArgument().setPath(classpath);

        cmd.createArgument().setValue("/x-");
        cmd.createArgument().setValue("/nomessage");
        cmd.createArgument().setValue("/nologo");

        if (debug) {
            cmd.createArgument().setValue("/g");
        }
        if (optimize) {
            cmd.createArgument().setValue("/O");
        }
        if (verbose) {
            cmd.createArgument().setValue("/verbose");
        }

        addCurrentCompilerArgs(cmd);

        int firstFileName = cmd.size();
        logAndAddFilesToCompile(cmd);

        return 
            executeExternalCompile(cmd.getCommandline(), firstFileName) == 0;
    }
}
