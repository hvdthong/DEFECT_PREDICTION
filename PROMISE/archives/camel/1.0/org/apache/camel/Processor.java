package org.apache.camel;

/**
 * is used to implement the
 * patterns and to process message exchanges.
 *
 * @version $Revision: 534145 $
 */
public interface Processor {

    /**
     * Processes the message exchange
     * 
     * @throws Exception if an internal processing error has occurred. 
     */
    void process(Exchange exchange) throws Exception;
}
