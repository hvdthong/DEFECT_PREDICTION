package org.apache.camel.component.spring.integration.adapter;

import org.apache.camel.CamelContext;
import org.apache.camel.Consumer;
import org.apache.camel.Endpoint;
import org.springframework.integration.handler.MessageHandler;

/**
 * The Abstract class for the Spring Integration Camel Adapter
 *
 * @author Willem Jiang
 *
 * @version $Revision: 655008 $
 */
public abstract class AbstractCamelAdapter implements MessageHandler {
    private CamelContext camelContext;
    private String camelEndpointUri;
    private volatile boolean expectReply = true;

    public void setCamelContext(CamelContext context) {
        camelContext = context;
    }

    public CamelContext getCamelContext() {
        return camelContext;
    }

    public String getCamelEndpointUri() {
        return camelEndpointUri;
    }

    public void setCamelEndpointUri(String uri) {
        camelEndpointUri = uri;
    }

    public void setExpectReply(boolean expectReply) {
        this.expectReply = expectReply;
    }

    public boolean isExpectReply() {
        return expectReply;
    }


}
