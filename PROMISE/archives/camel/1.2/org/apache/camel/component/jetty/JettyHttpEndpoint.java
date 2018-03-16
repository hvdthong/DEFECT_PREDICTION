package org.apache.camel.component.jetty;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.camel.Consumer;
import org.apache.camel.PollingConsumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.component.http.HttpConsumer;
import org.apache.camel.component.http.HttpEndpoint;
import org.apache.camel.component.http.HttpExchange;
import org.apache.camel.impl.EventDrivenPollingConsumer;

/**
 * @version $Revision: 1.1 $
 */
public class JettyHttpEndpoint extends HttpEndpoint {
    private JettyHttpComponent component;

    public JettyHttpEndpoint(JettyHttpComponent component, String uri, URI httpURL) throws URISyntaxException {
        super(uri, component, httpURL);
        this.component = component;
    }

    @Override
    public Producer<HttpExchange> createProducer() throws Exception {
        return super.createProducer();
    }

    @Override
    public Consumer<HttpExchange> createConsumer(Processor processor) throws Exception {
        return new HttpConsumer(this, processor);
    }

    @Override
    public PollingConsumer<HttpExchange> createPollingConsumer() throws Exception {
        return new EventDrivenPollingConsumer(this);
    }

    @Override
    public JettyHttpComponent getComponent() {
        return component;
    }
}
