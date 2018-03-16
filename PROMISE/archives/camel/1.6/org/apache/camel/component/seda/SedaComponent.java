package org.apache.camel.component.seda;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultComponent;

/**
 * for asynchronous SEDA exchanges on a {@link BlockingQueue} within a CamelContext
 *
 * @version $Revision: 655440 $
 */
public class SedaComponent extends DefaultComponent<Exchange> {

    public BlockingQueue<Exchange> createQueue(String uri, Map parameters) {
        int size = getAndRemoveParameter(parameters, "size", Integer.class, 1000);
        return new LinkedBlockingQueue<Exchange>(size);
    }

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        return new SedaEndpoint(uri, this, parameters);
    }
}
