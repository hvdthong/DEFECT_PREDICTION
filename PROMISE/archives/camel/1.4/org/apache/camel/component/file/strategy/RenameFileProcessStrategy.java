package org.apache.camel.component.file.strategy;

import java.io.File;

import org.apache.camel.component.file.FileEndpoint;
import org.apache.camel.component.file.FileExchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A strategy to rename a file
 * 
 * @version $Revision: 660099 $
 */
public class RenameFileProcessStrategy extends FileProcessStrategySupport {
    private static final transient Log LOG = LogFactory.getLog(RenameFileProcessStrategy.class);
    private FileRenamer renamer;

    public RenameFileProcessStrategy() {
        this(true);
    }

    public RenameFileProcessStrategy(boolean lock) {
        this(lock, ".camel/", "");
    }

    public RenameFileProcessStrategy(boolean lock, String namePrefix, String namePostfix) {
        this(lock, new DefaultFileRenamer(namePrefix, namePostfix));
    }

    public RenameFileProcessStrategy(boolean lock, FileRenamer renamer) {
        super(lock);
        this.renamer = renamer;
    }

    @Override
    public void commit(FileEndpoint endpoint, FileExchange exchange, File file) throws Exception {
        File newName = renamer.renameFile(file);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Renaming file: " + file + " to: " + newName);
        }

        if (newName.exists()) {
            newName.delete();
        }

        newName.getParentFile().mkdirs();

        boolean renamed = file.renameTo(newName);
        if (!renamed) {
            LOG.warn("Could not rename file from: " + file + " to " + newName);
        }

        super.commit(endpoint, exchange, file);
    }

    public FileRenamer getRenamer() {
        return renamer;
    }

    public void setRenamer(FileRenamer renamer) {
        this.renamer = renamer;
    }
}
