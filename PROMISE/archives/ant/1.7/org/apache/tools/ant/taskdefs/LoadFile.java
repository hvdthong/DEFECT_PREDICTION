package org.apache.tools.ant.taskdefs;

import java.io.File;
import org.apache.tools.ant.types.resources.FileResource;


/**
 * Load a file into a property
 *
 * @since Ant 1.5
 * @ant.task category="utility"
 */
public class LoadFile extends LoadResource {

    /**
     * Sets the file to load.
     *
     * @param srcFile The new SrcFile value
     */
    public final void setSrcFile(final File srcFile) {
        addConfigured(new FileResource(srcFile));
    }
}
