package org.apache.camel;

/**
 * used to implement the 
 * Event Driven Consumer</a> and 
 * Message Translator</a> patterns and to process message exchanges.
 * 
 * @version $Revision: 662664 $
 */
public interface Processor {

    /**
     * Processes the message exchange
     * 
     * @throws Exception if an internal processing error has occurred.
     */
    void process(Exchange exchange) throws Exception;
}
