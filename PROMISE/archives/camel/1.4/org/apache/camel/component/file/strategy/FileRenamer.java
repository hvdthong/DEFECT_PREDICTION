package org.apache.camel.component.file.strategy;

import java.io.File;

/**
 * Used for renaming files.
 *
 * @version $Revision: 647463 $
 */
public interface FileRenamer {

    /**
     * Renames the given file
     *
     * @param file  the original file.
     * @return  the renamed file.
     */
    File renameFile(File file);
}
