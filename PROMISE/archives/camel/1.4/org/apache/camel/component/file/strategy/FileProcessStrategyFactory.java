package org.apache.camel.component.file.strategy;

import java.util.Properties;

import org.apache.camel.component.file.FileProcessStrategy;

/**
 * Factory to provide the {@link org.apache.camel.component.file.FileProcessStrategy} to use.
 */
public final class FileProcessStrategyFactory {

    private FileProcessStrategyFactory() {
    }

    /**
     * A strategy method to lazily create the file strategy to use.
     */
    public static FileProcessStrategy createFileProcessStrategy(Properties params) {

        boolean isDelete = params.getProperty("delete") != null;
        boolean isLock = params.getProperty("lock") != null;
        String moveNamePrefix = params.getProperty("moveNamePrefix");
        String moveNamePostfix = params.getProperty("moveNamePostfix");

        if (params.getProperty("noop") != null) {
            return new NoOpFileProcessStrategy(isLock);
        } else if (moveNamePostfix != null || moveNamePrefix != null) {
            if (isDelete) {
                throw new IllegalArgumentException("You cannot set the deleteFiles property "
                    + "and a moveFilenamePostfix or moveFilenamePrefix");
            }
            return new RenameFileProcessStrategy(isLock, moveNamePrefix, moveNamePostfix);
        } else if (isDelete) {
            return new DeleteFileProcessStrategy(isLock);
        } else {
            return new RenameFileProcessStrategy(isLock);
        }
    }
}
