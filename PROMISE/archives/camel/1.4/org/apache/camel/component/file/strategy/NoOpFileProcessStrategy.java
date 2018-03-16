package org.apache.camel.component.file.strategy;

/**
 * A simple strategy which does not move or delete the processed files in any way.
 *
 * @version $Revision: 660102 $
 */
public class NoOpFileProcessStrategy extends FileProcessStrategySupport {

    public NoOpFileProcessStrategy() {
        super(true);
    }

    public NoOpFileProcessStrategy(boolean isLock) {
        super(isLock);
    }

}
