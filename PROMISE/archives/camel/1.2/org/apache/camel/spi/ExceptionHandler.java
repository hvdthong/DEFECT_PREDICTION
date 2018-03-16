package org.apache.camel.spi;

/**
 * A Strategy pattern for handling exceptions; particularly in asynchronous processes such as consumers
 *
 * @version $Revision: 1.1 $
 */
public interface ExceptionHandler {
    
    /**
     * Handles the given exception
     *
     * @param exception the exception
     */
    void handleException(Throwable exception);
}
