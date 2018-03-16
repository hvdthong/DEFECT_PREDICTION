package org.apache.tools.ant.taskdefs.optional.perforce;

import org.apache.tools.ant.BuildException;

/** Interface for p4 job output stream handler. Classes implementing this interface
 * can be called back by P4Base.execP4Command();
 *
 * @author <A HREF="mailto:leslie.hughes@rubus.com">Les Hughes</A>
 */
public interface P4OutputHandler {

    public void process(String line) throws BuildException;
}
