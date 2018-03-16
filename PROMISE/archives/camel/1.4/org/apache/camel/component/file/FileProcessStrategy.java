package org.apache.camel.component.file;

import java.io.File;

/**
 * Represents a strategy for marking that a file is processed.
 *
 * @version $Revision: 647463 $
 */
public interface FileProcessStrategy {

    /**
     * Called when work is about to begin on this file. This method may attempt to acquire some file lock before
     * returning true; returning false if the file lock could not be obtained so that the file should be ignored.
     *
     * @param endpoint  the endpoint
     * @param exchange  the exchange
     * @param file      the file
     * @return true if the file can be processed (such as if a file lock could be obtained)
     * @throws Exception can be thrown in case of errors
     */
    boolean begin(FileEndpoint endpoint, FileExchange exchange, File file) throws Exception;

    /**
     * Releases any file locks and possibly deletes or moves the file
     *
     * @param endpoint  the endpoint
     * @param exchange  the exchange
     * @param file      the file
     * @throws Exception can be thrown in case of errors
     */
    void commit(FileEndpoint endpoint, FileExchange exchange, File file) throws Exception;
    
}
