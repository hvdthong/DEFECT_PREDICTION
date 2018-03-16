package org.apache.camel.component.file.strategy;

import java.io.File;

import org.apache.camel.component.file.FileExchange;

/**
 * Used for renaming files.
 *
 * @version $Revision: 687736 $
 */
public interface FileRenamer {

    /**
     * Renames the given file
     *
     * @param exchange  the exchange
     * @param file      the original file.
     * @return the renamed file.
     */
    File renameFile(FileExchange exchange, File file);
}
