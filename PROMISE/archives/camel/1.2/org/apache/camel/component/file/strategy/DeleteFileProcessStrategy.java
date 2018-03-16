package org.apache.camel.component.file.strategy;

import java.io.File;

import org.apache.camel.component.file.FileEndpoint;
import org.apache.camel.component.file.FileExchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A strategy which will delete the file when its processed
 *
 * @version $Revision: 1.1 $
 */
public class DeleteFileProcessStrategy extends FileProcessStrategySupport {
    private static final transient Log LOG = LogFactory.getLog(DeleteFileProcessStrategy.class);

    public DeleteFileProcessStrategy() {
    }

    public DeleteFileProcessStrategy(boolean lockFile) {
        super(lockFile);
    }

    public void commit(FileEndpoint endpoint, FileExchange exchange, File file) throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Deleting file: " + file);
        }
        file.delete();
        super.commit(endpoint, exchange, file);
    }
}
