package org.apache.tools.ant.taskdefs.optional.javah;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.optional.Javah;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.JavaEnvUtils;

/**
 * Adapter to the native kaffeh compiler.
 *
 * @since Ant 1.6.3
 */
public class Kaffeh implements JavahAdapter {

    public static final String IMPLEMENTATION_NAME = "kaffeh";

    /**
     * Performs the actual compilation.
     *
     * @since Ant 1.6.3
     */
    public boolean compile(Javah javah) throws BuildException {
        Commandline cmd = setupKaffehCommand(javah);
        try {
            Execute.runCommand(javah, cmd.getCommandline());
            return true;
        } catch (BuildException e) {
            if (e.getMessage().indexOf("failed with return code") == -1) {
                throw e;
            }
        }
        return false;
    }

    private Commandline setupKaffehCommand(Javah javah) {
        Commandline cmd = new Commandline();
        cmd.setExecutable(JavaEnvUtils.getJdkExecutable("kaffeh"));

        if (javah.getDestdir() != null) {
            cmd.createArgument().setValue("-d");
            cmd.createArgument().setFile(javah.getDestdir());
        }

        if (javah.getOutputfile() != null) {
            cmd.createArgument().setValue("-o");
            cmd.createArgument().setFile(javah.getOutputfile());
        }

        Path cp = new Path(javah.getProject());
        if (javah.getBootclasspath() != null) {
            cp.append(javah.getBootclasspath());
        }
        if (javah.getClasspath() != null) {
            cp.append(javah.getClasspath());
        }
        if (cp.size() > 0) {
            cmd.createArgument().setValue("-classpath");
            cmd.createArgument().setPath(cp);
        }

        if (!javah.getOld()) {
            cmd.createArgument().setValue("-jni");
        }

        cmd.addArguments(javah.getCurrentArgs());

        javah.logAndAddFiles(cmd);
        return cmd;
    }

}
