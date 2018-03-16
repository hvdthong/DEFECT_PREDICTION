package org.apache.tools.ant.taskdefs.optional.unix;

import org.apache.tools.ant.BuildException;

/**
 * Chgrp equivalent for unix-like environments.
 *
 *
 * @since Ant 1.6
 *
 * @ant.task category="filesystem"
 */
public class Chgrp extends AbstractAccessTask {

    private boolean haveGroup = false;

    /**
     * Chgrp task for setting unix group of a file.
     */
    public Chgrp() {
        super.setExecutable("chgrp");
    }

    /**
     * Set the group atribute.
     *
     * @param group    The new group for the file(s) or directory(ies)
     */
    public void setGroup(String group) {
        createArg().setValue(group);
        haveGroup = true;
    }

    /**
     * Ensure that all the required arguments and other conditions have
     * been set.
     */
    protected void checkConfiguration() {
        if (!haveGroup) {
            throw new BuildException("Required attribute group not set in "
                                     + "chgrp", getLocation());
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
