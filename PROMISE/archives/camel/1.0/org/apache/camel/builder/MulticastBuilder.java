package org.apache.camel.builder;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.processor.MulticastProcessor;

import java.util.Collection;

/**
 * A builder for the {@link MulticastProcessor} pattern
 *
 * @version $Revision: 534145 $
 */
public class MulticastBuilder extends FromBuilder {
    private final Collection<Endpoint> endpoints;

    public MulticastBuilder(FromBuilder parent, Collection<Endpoint> endpoints) {
        super(parent);
        this.endpoints = endpoints;
    }

    @Override
    public Processor createProcessor() throws Exception {
        return new MulticastProcessor(endpoints);
    }
}
