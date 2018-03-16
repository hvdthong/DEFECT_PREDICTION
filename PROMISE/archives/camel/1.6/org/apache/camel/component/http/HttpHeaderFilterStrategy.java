package org.apache.camel.component.http;

import org.apache.camel.impl.DefaultHeaderFilterStrategy;

/**
 * @version $Revision: 691703 $
 */
public class HttpHeaderFilterStrategy extends DefaultHeaderFilterStrategy {
    
    public HttpHeaderFilterStrategy() {
        initialize();  
    }

    protected void initialize() {
        getOutFilter().add("content-length");
        getOutFilter().add("content-type");
        getOutFilter().add(HttpMethods.HTTP_METHOD);
        getOutFilter().add(HttpProducer.QUERY);
        getOutFilter().add(HttpProducer.HTTP_RESPONSE_CODE.toLowerCase());
        setIsLowercase(true);
        
        setOutFilterPattern("(org\\.apache\\.camel)[\\.|a-z|A-z|0-9]*");    
    }
}
