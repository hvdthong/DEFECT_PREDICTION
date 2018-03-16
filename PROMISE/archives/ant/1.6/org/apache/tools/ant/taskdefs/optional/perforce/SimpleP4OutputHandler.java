package org.apache.tools.ant.taskdefs.optional.perforce;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.util.StringUtils;

/**
 * simple implementation of P4HandlerAdapter used by tasks which are not
 * actually processing the output from Perforce
 */
public class SimpleP4OutputHandler extends P4HandlerAdapter {

    P4Base parent;

    /**
     * simple constructor
     * @param parent  a P4Base instance
     */
    public SimpleP4OutputHandler(P4Base parent) {
        this.parent = parent;
    }

    /**
     * process one line of stderr/stdout
     * if error conditions are detected, then setters are called on the
     * parent
     * @param line line of output
     * @throws BuildException does not throw exceptions any more
     */
    public void process(String line) throws BuildException {
        if (parent.util.match("/^exit/", line)) {
            return;
        }


        if (parent.util.match("/^error:/", line)
            || parent.util.match("/^Perforce client error:/", line)) {
            if (!parent.util.match("/label in sync/", line)
                && !parent.util.match("/up-to-date/", line)) {
                parent.setInError(true);
            } else {
            }
        } else if (parent.util.match("/^info.*?:/", line)) {
        }
        parent.log(line, parent.getInError() ? Project.MSG_ERR : Project.MSG_INFO);

        if (parent.getInError()) {
            parent.setErrorMessage(parent.getErrorMessage() + line + StringUtils.LINE_SEP);
        }
    }
}
