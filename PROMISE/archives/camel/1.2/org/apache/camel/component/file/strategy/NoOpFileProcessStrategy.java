package org.apache.camel.component.file.strategy;

/**
 * A simple strategy which just locks the file but does not modify it
 *
 * @version $Revision: 1.1 $
 */
public class NoOpFileProcessStrategy extends FileProcessStrategySupport {
    public NoOpFileProcessStrategy() {
        super(false);
    }

}

