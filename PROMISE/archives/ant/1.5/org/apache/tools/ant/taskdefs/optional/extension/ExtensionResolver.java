package org.apache.tools.ant.taskdefs.optional.extension;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

/**
 * Interface to locate a File that satisfies extension.
 *
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 * @author <a href="mailto:jeff@socialchange.net.au">Jeff Turner</>
 * @version $Revision: 274041 $ $Date: 2003-02-10 22:25:38 +0800 (周一, 2003-02-10) $
 */
public interface ExtensionResolver
{
    /**
     * Attempt to locate File that satisfies
     * extension via resolver.
     *
     * @param extension the extension
     * @return the File satisfying extension, null
     *         if can not resolve extension
     * @throws BuildException if error occurs attempting to
     *         resolve extension
     */
    File resolve( Extension extension, Project project )
        throws BuildException;
}
