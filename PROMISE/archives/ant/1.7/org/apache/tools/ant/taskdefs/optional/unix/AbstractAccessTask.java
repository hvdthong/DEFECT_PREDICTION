package org.apache.tools.ant.taskdefs.optional.unix;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.condition.Os;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.FileSet;

/**
 * @since Ant 1.6
 *
 * @ant.task category="filesystem"
 */

public abstract class AbstractAccessTask
    extends org.apache.tools.ant.taskdefs.ExecuteOn {

    /**
     * Chmod task for setting file and directory permissions.
     */
    public AbstractAccessTask() {
        super.setParallel(true);
        super.setSkipEmptyFilesets(true);
    }

    /**
     * Set the file which should have its access attributes modified.
     * @param src the file to modify
     */
    public void setFile(File src) {
        FileSet fs = new FileSet();
        fs.setFile(src);
        addFileset(fs);
    }

    /**
     * Prevent the user from specifying a different command.
     *
     * @ant.attribute ignore="true"
     * @param cmdl A user supplied command line that we won't accept.
     */
    public void setCommand(Commandline cmdl) {
        throw new BuildException(getTaskType()
                                 + " doesn\'t support the command attribute",
                                 getLocation());
    }

    /**
     * Prevent the skipping of empty filesets
     *
     * @ant.attribute ignore="true"
     * @param skip A user supplied boolean we won't accept.
     */
    public void setSkipEmptyFilesets(boolean skip) {
        throw new BuildException(getTaskType() + " doesn\'t support the "
                                 + "skipemptyfileset attribute",
                                 getLocation());
    }

    /**
     * Prevent the use of the addsourcefile atribute.
     *
     * @ant.attribute ignore="true"
     * @param b A user supplied boolean we won't accept.
     */
    public void setAddsourcefile(boolean b) {
        throw new BuildException(getTaskType()
            + " doesn\'t support the addsourcefile attribute", getLocation());
    }

    /**
     * Automatically approve Unix OS's.
     * @return true if a valid OS, for unix this is always true, otherwise
     *              use the superclasses' test (user set).
     */
    protected boolean isValidOs() {
        return Os.isFamily("unix") && super.isValidOs();
    }
}
