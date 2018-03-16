package org.apache.camel.component.restlet;

import java.io.IOException;

import org.apache.camel.Exchange;
import org.restlet.data.Request;
import org.restlet.data.Response;

/**
 * Interface for converting between Camel message and Restlet message.
 * 
 * @version $Revision: 730506 $
 */
public interface RestletBinding {
    
    /**
     * Populate Restlet request from Camel message
     *  
     * @param exchange message to be copied from 
     * @param response to be populated
     */
    void populateRestletResponseFromExchange(Exchange exchange,
            Response response);

    /**
     * Populate Camel message from Restlet request
     * 
     * @param request message to be copied from
     * @param exchange to be populated
     * @throws Exception 
     */
    void populateExchangeFromRestletRequest(Request request, 
            Exchange exchange) throws Exception;

    /**
     * Populate Restlet Request from Camel message
     * 
     * @param request to be populated
     * @param exchange message to be copied from
     */
    void populateRestletRequestFromExchange(Request request, Exchange exchange);

    /**
     * Populate Camel message from Restlet response
     * 
     * @param exchange to be populated
     * @param response message to be copied from
     * @throws IOException 
     */
    void populateExchangeFromRestletResponse(Exchange exchange,
            Response response) throws IOException;

}
