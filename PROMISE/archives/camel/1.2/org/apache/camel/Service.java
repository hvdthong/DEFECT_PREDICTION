package org.apache.camel;

/**
 * Represents the core lifecycle API for POJOs which can be started and stopped
 * 
 * @version $Revision: 563607 $
 */
public interface Service {

    /**
     * Starts the service
     * 
     * @throws Exception
     */
    void start() throws Exception;

    /**
     * Stops the service
     * 
     * @throws Exception
     */
    void stop() throws Exception;
}
