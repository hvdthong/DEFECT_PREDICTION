package org.apache.camel.component.cxf;

import org.apache.cxf.message.Exchange;

/**
 * The interface to provide a CXF message invoke method
 */
public interface MessageInvoker {

    /**
     * This method is called when the incoming message is to be passed into the
     * camel processor. The return value is the response from the processor
     *
     * @param exchange the CXF exchange which holds the in and out message
     */
    void invoke(Exchange exchange);

}
