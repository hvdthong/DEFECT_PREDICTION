package org.apache.camel.component.xmpp;

import java.net.URI;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.HeaderFilterStrategyAware;
import org.apache.camel.impl.DefaultComponent;
import org.apache.camel.impl.DefaultHeaderFilterStrategy;
import org.apache.camel.spi.HeaderFilterStrategy;

/**
 * @version $Revision:520964 $
 */
public class XmppComponent extends DefaultComponent<XmppExchange> implements HeaderFilterStrategyAware {

    private HeaderFilterStrategy headerFilterStrategy = new DefaultHeaderFilterStrategy();

    public XmppComponent() {
    }

    public XmppComponent(CamelContext context) {
        super(context);
    }

    /**
     * Static builder method
     */
    public static XmppComponent xmppComponent() {
        return new XmppComponent();
    }

    @Override
    protected Endpoint<XmppExchange> createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        XmppEndpoint endpoint = new XmppEndpoint(uri, this);

        URI u = new URI(uri);
        endpoint.setHost(u.getHost());
        endpoint.setPort(u.getPort());
        if (u.getUserInfo() != null) {
            endpoint.setUser(u.getUserInfo());
        }
        String remainingPath = u.getPath();
        if (remainingPath != null) {
            if (remainingPath.startsWith("/")) {
                remainingPath = remainingPath.substring(1);
            }

            if (remainingPath.length() > 0) {
                endpoint.setParticipant(remainingPath);
            }
        }
        return endpoint;
    }

    public HeaderFilterStrategy getHeaderFilterStrategy() {
        return headerFilterStrategy;
    }

    public void setHeaderFilterStrategy(HeaderFilterStrategy strategy) {
        headerFilterStrategy = strategy;
        
    }
}
