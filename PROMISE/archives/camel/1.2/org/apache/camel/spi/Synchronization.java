package org.apache.camel.spi;

import org.apache.camel.Component;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * Provides a hook for custom {@link Processor} or {@link Component} instances to respond to
 * completed or failed processing of an {@link Exchange} rather like Spring's
 *
 * @version $Revision: 1.1 $
 */
public interface Synchronization {

    /**
     * Called when the processing of the message exchange is complete
     *
     * @param exchange the excahnge being processed
     */
    void onComplete(Exchange exchange);

    /**
     * Called when the processing of the message exchange has failed for some reason. 
     * The exception which caused the problem is in {@link Exchange#getException()} and
     * there could be a fault message via {@link Exchange#getFault()}
     *
     * @param exchange the excahnge being processed
     */
    void onFailure(Exchange exchange);
}
