package org.apache.camel.component.jhc;

import java.net.URI;
import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.HeaderFilterStrategyAware;
import org.apache.camel.impl.DefaultComponent;
import org.apache.camel.impl.DefaultHeaderFilterStrategy;
import org.apache.camel.spi.HeaderFilterStrategy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

public class JhcComponent extends DefaultComponent<JhcExchange> implements HeaderFilterStrategyAware {

    private static final Log LOG = LogFactory.getLog(JhcComponent.class);

    private HttpParams params;

    private HeaderFilterStrategy headerFilterStrategy;

    public JhcComponent() {
     
        setHeaderFilterStrategy(new JhcHeaderFilterStrategy());
        
        params = new BasicHttpParams()
            .setIntParameter(HttpConnectionParams.SO_TIMEOUT, 5000)
            .setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 10000)
            .setIntParameter(HttpConnectionParams.SOCKET_BUFFER_SIZE, 8 * 1024)
            .setBooleanParameter(HttpConnectionParams.STALE_CONNECTION_CHECK, false)
            .setBooleanParameter(HttpConnectionParams.TCP_NODELAY, true)
            .setParameter(HttpProtocolParams.USER_AGENT, "Camel-JhcComponent/1.1");
    }

    public HttpParams getParams() {
        return params;
    }

    public void setParams(HttpParams params) {
        this.params = params;
    }

    protected Endpoint<JhcExchange> createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        return new JhcEndpoint(uri, this, new URI(uri.substring(uri.indexOf(':') + 1)));
    }

    public HeaderFilterStrategy getHeaderFilterStrategy() {
        return headerFilterStrategy;
    }

    public void setHeaderFilterStrategy(HeaderFilterStrategy strategy) {
        headerFilterStrategy = strategy;
    }

}
