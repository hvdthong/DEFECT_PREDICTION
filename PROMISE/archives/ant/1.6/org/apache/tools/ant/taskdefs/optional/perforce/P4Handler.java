package org.apache.tools.ant.taskdefs.optional.perforce;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.ExecuteStreamHandler;

/** Interface for p4 job output stream handler. Classes implementing this interface
 * can be called back by P4Base.execP4Command();
 *
 */
public interface P4Handler extends ExecuteStreamHandler {

    /**
     * processing of one line of stdout or of stderr
     * @param line a line of stdout or stderr that the implementation will process
     * @throws BuildException at the discretion of the implementation.
     */
    void process(String line) throws BuildException;

    /**
     * set any data to be written to P4's stdin
     * @param line the text to write to P4's stdin
     * @throws BuildException if the line cannot be processed.
     */
    void setOutput(String line) throws BuildException;
}
