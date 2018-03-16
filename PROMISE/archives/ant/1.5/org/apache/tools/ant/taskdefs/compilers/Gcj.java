package org.apache.tools.ant.taskdefs.compilers;


import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Path;

/**
 * The implementation of the gcj compiler.
 * This is primarily a cut-and-paste from the jikes.
 *
 * @author <a href="mailto:tora@debian.org">Takashi Okamoto</a>
 * @since Ant 1.4
 */
public class Gcj extends DefaultCompilerAdapter {

    /**
     * Performs a compile using the gcj compiler.
     */
    public boolean execute() throws BuildException {
        Commandline cmd;
        attributes.log("Using gcj compiler", Project.MSG_VERBOSE);
        cmd = setupGCJCommand();

        int firstFileName = cmd.size();
        logAndAddFilesToCompile(cmd);

        return 
            executeExternalCompile(cmd.getCommandline(), firstFileName) == 0;
    }

    protected Commandline setupGCJCommand() {
        Commandline cmd = new Commandline();
        Path classpath = new Path(project);

        if (bootclasspath != null) {
            classpath.append(bootclasspath);
        }

        classpath.addExtdirs(extdirs);

        if (bootclasspath == null || bootclasspath.size() == 0) {
            includeJavaRuntime = true;
        }
        classpath.append(getCompileClasspath());

        if (compileSourcepath != null) {
            classpath.append(compileSourcepath);
        } else {
            classpath.append(src);
        }

        cmd.setExecutable("gcj");

        if (destDir != null) {
            cmd.createArgument().setValue("-d");
            cmd.createArgument().setFile(destDir);
            
            if (destDir.mkdirs()) {
                throw new BuildException("Can't make output directories. "
                                         + "Maybe permission is wrong. ");
            };
        }
        
        cmd.createArgument().setValue("-classpath");
        cmd.createArgument().setPath(classpath);

        if (encoding != null) {
            cmd.createArgument().setValue("--encoding=" + encoding);
        }
        if (debug) {
            cmd.createArgument().setValue("-g1");
        }
        if (optimize) {
            cmd.createArgument().setValue("-O");
        }

        /**
         *  gcj should be set for generate class.
         */
        cmd.createArgument().setValue("-C");

        addCurrentCompilerArgs(cmd);

        return cmd;
    }
}
