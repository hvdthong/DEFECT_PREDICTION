package org.apache.tools.ant.taskdefs.optional.unix;

import org.apache.tools.ant.BuildException;

/**
 * Chown equivalent for unix-like environments.
 *
 * @since Ant 1.6
 *
 * @ant.task category="filesystem"
 */
public class Chown extends AbstractAccessTask {

    private boolean haveOwner = false;

    /**
     * Chown task for setting file and directory permissions.
     */
    public Chown() {
        super.setExecutable("chown");
    }

    /**
     * Set the owner atribute.
     *
     * @param owner    The new owner for the file(s) or directory(ies)
     */
    public void setOwner(String owner) {
        createArg().setValue(owner);
        haveOwner = true;
    }

    /**
     * Ensure that all the required arguments and other conditions have
     * been set.
     */
    protected void checkConfiguration() {
        if (!haveOwner) {
            throw new BuildException("Required attribute owner not set in"
                                     + " chown", getLocation());
        }
        super.checkConfiguration();
    }

    /**
     * We don't want to expose the executable atribute, so overide it.
     *
     * @param e User supplied executable that we won't accept.
     */
    public void setExecutable(String e) {
        throw new BuildException(getTaskType()
                                 + " doesn\'t support the executable"
                                 + " attribute", getLocation());
    }
}
