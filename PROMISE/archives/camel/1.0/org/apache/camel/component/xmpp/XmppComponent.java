package org.apache.camel.component.xmpp;

import com.sun.jndi.toolkit.url.Uri;
import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;
import org.apache.camel.util.IntrospectionSupport;
import org.apache.camel.util.ObjectHelper;
import org.apache.camel.util.URISupport;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * @version $Revision:520964 $
 */
public class XmppComponent extends DefaultComponent<XmppExchange> {
    /**
     * Static builder method
     */
    public static XmppComponent xmppComponent() {
        return new XmppComponent();
    }

    public XmppComponent() {
    }

    public XmppComponent(CamelContext context) {
        super(context);
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
}
