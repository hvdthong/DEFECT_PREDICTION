package org.apache.tools.ant.taskdefs.optional.perforce;

import org.apache.tools.ant.BuildException;

/** Interface for p4 job output stream handler. Classes implementing this interface
 * can be called back by P4Base.execP4Command();
 *
 */
public interface P4OutputHandler {
    /**
     * implementations will be able to process lines of output from Perforce
     * @param line a line of stdout or stderr coming from Perforce
     * @throws BuildException implementations are allowed to throw BuildException
     */
    void process(String line) throws BuildException;
}
