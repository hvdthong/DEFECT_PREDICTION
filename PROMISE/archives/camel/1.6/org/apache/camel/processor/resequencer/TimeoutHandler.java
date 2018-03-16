package org.apache.camel.processor.resequencer;

/**
 * Implemented by classes that handle timeout notifications.
 * 
 * @author Martin Krasser
 * 
 * @version $Revision: 697732 $
 */
public interface TimeoutHandler {

    /**
     * Handles a timeout notification.
     * 
     * @param timeout the timer task that caused this timeout notification.
     */
    void timeout(Timeout timeout);
    
}
