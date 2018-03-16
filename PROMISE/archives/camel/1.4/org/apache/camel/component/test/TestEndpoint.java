package org.apache.camel.component.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Component;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Service;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.util.EndpointHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * pull all messages from the nested endpoint and use those as expected message body assertions.
 *
 * @version $Revision: 655516 $
 */
public class TestEndpoint extends MockEndpoint implements Service {
    private static final transient Log LOG = LogFactory.getLog(TestEndpoint.class);
    private final Endpoint expectedMessageEndpoint;
    private long timeout = 2000L;

    public TestEndpoint(String endpointUri, Component component, Endpoint expectedMessageEndpoint) {
        super(endpointUri, component);
        this.expectedMessageEndpoint = expectedMessageEndpoint;
    }

    public TestEndpoint(String endpointUri, Endpoint expectedMessageEndpoint) {
        super(endpointUri);
        this.expectedMessageEndpoint = expectedMessageEndpoint;
    }

    public void start() throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Consuming expected messages from: " + expectedMessageEndpoint);
        }
        final List expectedBodies = new ArrayList();
        EndpointHelper.pollEndpoint(expectedMessageEndpoint, new Processor() {
            public void process(Exchange exchange) throws Exception {
                Object body = getInBody(exchange);
                expectedBodies.add(body);
            }
        }, timeout);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Received: " + expectedBodies.size() + " expected message(s) from: " + expectedMessageEndpoint);
        }
        expectedBodiesReceived(expectedBodies);
    }

    public void stop() throws Exception {
    }

    /**
     * This method allows us to convert or coerce the expected message body into some other type
     */
    protected Object getInBody(Exchange exchange) {
        return exchange.getIn().getBody();
    }
}
