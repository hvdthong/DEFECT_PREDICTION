package org.apache.camel.component.mock;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultComponent;
import org.apache.camel.processor.ThroughputLogger;

/**
 * A factory of {@link MockEndpoint} instances
 *
 * @version $Revision: 655440 $
 */
public class MockComponent extends DefaultComponent<Exchange> {

    @Override
    protected Endpoint<Exchange> createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        MockEndpoint endpoint = new MockEndpoint(uri, this);
        Integer value = getAndRemoveParameter(parameters, "reportGroup", Integer.class);
        if (value != null) {
            Processor reporter = new ThroughputLogger("org.apache.camel.mock:" + remaining, value);
            endpoint.setReporter(reporter);
        }
        return endpoint;
    }
}
