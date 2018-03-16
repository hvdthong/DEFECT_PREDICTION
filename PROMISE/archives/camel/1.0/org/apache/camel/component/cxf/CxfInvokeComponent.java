package org.apache.camel.component.cxf;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.CXFBusFactory;

import java.net.URI;
import java.util.Map;
import java.util.Properties;

/**
 * @version $Revision: 525898 $
 */
public class CxfInvokeComponent extends DefaultComponent<CxfExchange> {
    private Bus bus;

    public CxfInvokeComponent() {
        bus = CXFBusFactory.getDefaultBus();
    }

    public CxfInvokeComponent(CamelContext context) {
        super(context);
        bus = CXFBusFactory.getDefaultBus();
    }

    @Override
    protected Endpoint<CxfExchange> createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        return new CxfInvokeEndpoint(getAddress(remaining), this, getQueryAsProperties(new URI(remaining)));
    }

    /**
     * Read query parameters from uri
     *
     * @param u
     * @return parameter value pairs as properties
     */
    protected Properties getQueryAsProperties(URI u) {
        Properties retval = new Properties();
        if (u.getQuery() != null) {
            String[] parameters = u.getQuery().split("&");
            for (int i = 0; i < parameters.length; i++) {
                String[] s = parameters[i].split("=");
                retval.put(s[0], s[1]);
            }
        }
        return retval;
    }

    /**
     * Remove query from uri
     *
     * @param uri
     * @return substring before  the "?" character
     */
    protected String getAddress(String uri) {
        int index = uri.indexOf("?");
        if (-1 != index) {
            return uri.substring(0, index);
        }
        return uri;
    }

    public Bus getBus() {
        return bus;
    }
}
