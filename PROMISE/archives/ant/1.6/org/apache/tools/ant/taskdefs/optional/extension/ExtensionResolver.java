package org.apache.tools.ant.taskdefs.optional.extension;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

/**
 * Interface to locate a File that satisfies extension.
 *
 */
public interface ExtensionResolver {
    /**
     * Attempt to locate File that satisfies
     * extension via resolver.
     *
     * @param extension the extension
     * @param project the Ant project instance
     * @return the File satisfying extension, null
     *         if can not resolve extension
     * @throws BuildException if error occurs attempting to
     *         resolve extension
     */
    File resolve(Extension extension, Project project)
        throws BuildException;
}
