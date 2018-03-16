package org.apache.tools.ant.taskdefs.compilers;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Apt;
import org.apache.tools.ant.types.Commandline;

/**
 * The implementation of the apt compiler for JDK 1.5 using an external process
 *
 * @since Ant 1.7
 */
public class AptExternalCompilerAdapter extends DefaultCompilerAdapter {


    /**
     * Get the facade task that fronts this adapter
     *
     * @return task instance
     * @see DefaultCompilerAdapter#getJavac()
     */
    protected Apt getApt() {
        return (Apt) getJavac();
    }

    /**
     * Performs a compile using the Javac externally.
     * @return true  the compilation was successful.
     * @throws BuildException if there is a problem.
     */
    public boolean execute() throws BuildException {
        attributes.log("Using external apt compiler", Project.MSG_VERBOSE);


        Apt apt = getApt();
        Commandline cmd = new Commandline();
        cmd.setExecutable(apt.getAptExecutable());
        setupModernJavacCommandlineSwitches(cmd);
        AptCompilerAdapter.setAptCommandlineSwitches(apt, cmd);
        int firstFileName = cmd.size();
        logAndAddFilesToCompile(cmd);

        return 0 == executeExternalCompile(cmd.getCommandline(),
                firstFileName,
                true);

    }

}

