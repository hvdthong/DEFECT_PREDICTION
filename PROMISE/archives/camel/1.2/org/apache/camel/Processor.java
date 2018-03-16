package org.apache.camel;

/**
 * used to implement the <a
 * Driven Consumer</a> and <a
 * Translater</a> patterns and to process message exchanges.
 * 
 * @version $Revision: 580990 $
 */
public interface Processor {

    /**
     * Processes the message exchange
     * 
     * @throws Exception if an internal processing error has occurred.
     */
    void process(Exchange exchange) throws Exception;
}
