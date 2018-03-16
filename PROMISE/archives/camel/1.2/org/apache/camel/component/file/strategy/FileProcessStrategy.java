package org.apache.camel.component.file.strategy;

import java.io.File;

import org.apache.camel.component.file.FileEndpoint;
import org.apache.camel.component.file.FileExchange;

/**
 * Represents a strategy for marking that a file is processed.
 *
 * @version $Revision: 1.1 $
 */
public interface FileProcessStrategy {
    /**
     * Called when work is about to begin on this file. This method may attempt to acquire some file lock before
     * returning true; returning false if the file lock could not be obtained so that the file should be ignored.
     *
     * @return true if the file can be processed (such as if a file lock could be obtained)
     */
    boolean begin(FileEndpoint endpoint, FileExchange exchange, File file) throws Exception;

    /**
     * Releases any file locks and possibly deletes or moves the file
     */
    void commit(FileEndpoint endpoint, FileExchange exchange, File file) throws Exception;
}
