package org.apache.tools.ant.taskdefs.compilers;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Path;

/**
 * The implementation of the jvc compiler from microsoft.
 * This is primarily a cut-and-paste from the original javac task before it
 * was refactored.
 *
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

        if (includeJavaRuntime) {
            classpath.addExtdirs(extdirs);
        }

        classpath.append(getCompileClasspath());

        if (compileSourcepath != null) {
            classpath.append(compileSourcepath);
        } else {
            classpath.append(src);
        }

        Commandline cmd = new Commandline();
        String exec = getJavac().getExecutable();
        cmd.setExecutable(exec == null ? "jvc" : exec);

        if (destDir != null) {
            cmd.createArgument().setValue("/d");
            cmd.createArgument().setFile(destDir);
        }

        cmd.createArgument().setValue("/cp:p");
        cmd.createArgument().setPath(classpath);

        boolean msExtensions = true;
        String mse = getProject().getProperty("build.compiler.jvc.extensions");
        if (mse != null) {
            msExtensions = Project.toBoolean(mse);
        }

        if (msExtensions) {
            cmd.createArgument().setValue("/x-");
            cmd.createArgument().setValue("/nomessage");
        }

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
