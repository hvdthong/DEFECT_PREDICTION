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
 * @version $Revision: 1.1 $
 */
public class SedaComponent extends DefaultComponent {
    public BlockingQueue<Exchange> createQueue() {
        return new LinkedBlockingQueue<Exchange>(1000);
    }

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        return new SedaEndpoint(uri, this);
    }
}
