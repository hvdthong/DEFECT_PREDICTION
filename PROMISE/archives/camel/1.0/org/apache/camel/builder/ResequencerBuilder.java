package org.apache.camel.builder;

import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.Route;
import org.apache.camel.Exchange;
import org.apache.camel.Service;
import org.apache.camel.processor.Resequencer;

import java.util.List;

/**
 * @version $Revision: 1.1 $
 */
public class ResequencerBuilder extends FromBuilder {
    private final List<Expression<Exchange>> expressions;
    private long batchTimeout = 1000L;
    private int batchSize = 100;

    public ResequencerBuilder(FromBuilder builder, List<Expression<Exchange>> expressions) {
        super(builder);
        this.expressions = expressions;
    }

    @Override
    public Route createRoute() throws Exception {
        final Processor processor = super.createProcessor();
        final Resequencer resequencer = new Resequencer(getFrom(), processor, expressions);

        return new Route<Exchange>(getFrom()) {
            protected void addServices(List<Service> list) throws Exception {
                list.add(resequencer);
            }

            @Override
            public String toString() {
                return "ResequencerRoute[" + getEndpoint() + " -> " + processor + "]";
            }
        };
    }

    public ResequencerBuilder batchSize(int batchSize) {
        setBatchSize(batchSize);
        return this;
    }

    public ResequencerBuilder batchTimeout(int batchTimeout) {
        setBatchTimeout(batchTimeout);
        return this;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public long getBatchTimeout() {
        return batchTimeout;
    }

    public void setBatchTimeout(long batchTimeout) {
        this.batchTimeout = batchTimeout;
    }
}
