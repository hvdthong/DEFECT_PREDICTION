package org.apache.camel.processor;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Producer;

import java.util.Collection;

/**
 * Creates a Pipeline pattern where the output of the previous step is sent as input to the next step when working
 * with request/response message exchanges.
 *
 * @version $Revision: 534145 $
 */
public class Pipeline extends MulticastProcessor implements Processor {
    public Pipeline(Collection<Endpoint> endpoints) throws Exception {
        super(endpoints);
    }

    public void process(Exchange exchange) throws Exception {
        Exchange nextExchange = exchange;
        boolean first = true;
        for (Producer producer : getProducers()) {
            if (first) {
                first = false;
            }
            else {
                nextExchange = createNextExchange(producer, nextExchange);
            }
            producer.process(nextExchange);
        }
    }

    /**
     * Strategy method to create the next exchange from the
     *
     * @param producer         the producer used to send to the endpoint
     * @param previousExchange the previous exchange
     * @return a new exchange
     */
    protected Exchange createNextExchange(Producer producer, Exchange previousExchange) {
        Exchange answer = producer.createExchange(previousExchange);

        Object output = previousExchange.getOut().getBody();
        if (output != null) {
            answer.getIn().setBody(output);
        }
        return answer;
    }

    /**
     * Strategy method to copy the exchange before sending to another endpoint. Derived classes such as the
     * {@link Pipeline} will not clone the exchange
     *
     * @param exchange
     * @return the current exchange if no copying is required such as for a pipeline otherwise a new copy of the exchange is returned.
     */
    protected Exchange copyExchangeStrategy(Exchange exchange) {
        return exchange.copy();
    }

    @Override
    public String toString() {
        return "Pipeline" + getEndpoints();
    }
}
