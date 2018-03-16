package org.apache.camel.component.restlet;

import org.apache.camel.impl.DefaultHeaderFilterStrategy;

/**
 * Default header filtering strategy for Restlet
 * 
 * @version $Revision: 730572 $
 */
public class RestletHeaderFilterStrategy extends DefaultHeaderFilterStrategy {

    public RestletHeaderFilterStrategy() {
        
        getOutFilter().add(RestletConstants.LOGIN);
        getOutFilter().add(RestletConstants.PASSWORD);
    }
}
