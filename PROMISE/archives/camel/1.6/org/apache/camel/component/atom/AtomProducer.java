package org.apache.camel.component.atom;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * AtomProducer is currently not implemented
 *
 * @version $Revision: 656106 $
 */
public class AtomProducer extends DefaultProducer {
    private static final transient Log LOG = LogFactory.getLog(AtomProducer.class);
    private final AtomEndpoint endpoint;

    public AtomProducer(AtomEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

    public void process(Exchange exchange) throws Exception {
        throw new UnsupportedOperationException("AtomProducer is not implemented");
    }

}
