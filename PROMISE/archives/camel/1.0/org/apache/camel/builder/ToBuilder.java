package org.apache.camel.builder;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.processor.SendProcessor;

/**
 * @version $Revision: 534145 $
 */
public class ToBuilder<E extends Exchange> extends FromBuilder {
    private Endpoint destination;

    public ToBuilder(FromBuilder parent, Endpoint endpoint) {
        super(parent);
        this.destination = endpoint;
    }

    @Override
    public Processor createProcessor() {
        return new SendProcessor(destination);
    }
}
