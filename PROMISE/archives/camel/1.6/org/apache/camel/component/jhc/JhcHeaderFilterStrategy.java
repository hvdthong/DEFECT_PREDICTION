package org.apache.camel.component.jhc;

import org.apache.camel.impl.DefaultHeaderFilterStrategy;

/**
 * 
 * @version $Revision: 682597 $
 */
public class JhcHeaderFilterStrategy extends DefaultHeaderFilterStrategy {

    public JhcHeaderFilterStrategy() {
        initialize();
    }
    
    protected void initialize() {
        getOutFilter().add("content-length");
        getOutFilter().add("content-type");
        getOutFilter().add(JhcProducer.HTTP_RESPONSE_CODE);
        setIsLowercase(true);

        setOutFilterPattern("(org\\.apache\\.camel)[\\.|a-z|A-z|0-9]*");   
    }
}
