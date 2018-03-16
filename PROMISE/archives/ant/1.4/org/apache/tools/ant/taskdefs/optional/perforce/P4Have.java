package org.apache.tools.ant.taskdefs.optional.perforce;

import org.apache.tools.ant.*;


/** P4Have - lists files currently on client.
 * 
 * P4Have simply dumps the current file version info into
 * the Ant log (or stdout).
 * @author <A HREF="mailto:leslie.hughes@rubus.com">Les Hughes</A>
 */
public class P4Have extends P4Base {

    public void execute() throws BuildException {
        execP4Command("have "+P4CmdOpts+" "+P4View, new SimpleP4OutputHandler(this));
    }
}
