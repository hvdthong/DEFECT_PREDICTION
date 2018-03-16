package org.apache.tools.ant.taskdefs.optional.perforce;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

public class SimpleP4OutputHandler extends P4HandlerAdapter {

    P4Base parent;

    public SimpleP4OutputHandler(P4Base parent) {
        this.parent = parent;
    }

    public void process(String line) throws BuildException {
        if (parent.util.match("/^exit/", line)) {
            return;
        }


        if (parent.util.match("/error:/", line) && !parent.util.match("/up-to-date/", line)) {
            throw new BuildException(line);

        }


    }
}
