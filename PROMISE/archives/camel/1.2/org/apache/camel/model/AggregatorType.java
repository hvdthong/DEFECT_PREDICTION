package org.apache.camel.model;

import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.Route;
import org.apache.camel.impl.RouteContext;
import org.apache.camel.model.language.ExpressionType;
import org.apache.camel.processor.Aggregator;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.apache.camel.processor.aggregate.UseLatestAggregationStrategy;

/**
 * @version $Revision: 1.1 $
 */
@XmlRootElement(name = "aggregator")
@XmlAccessorType(XmlAccessType.FIELD)
public class AggregatorType extends ExpressionNode {
    @XmlTransient
    private AggregationStrategy aggregationStrategy = new UseLatestAggregationStrategy();
    private int batchSize;
    private long batchTimeout;
    public AggregatorType() {
    }

    public AggregatorType(Expression correlationExpression) {
        super(correlationExpression);
    }

    public AggregatorType(ExpressionType correlationExpression) {
        super(correlationExpression);
    }

    public AggregatorType(Expression correlationExpression, AggregationStrategy aggregationStrategy) {
        super(correlationExpression);
        this.aggregationStrategy = aggregationStrategy;
    }

    @Override
    public String toString() {
        return "Aggregator[ " + getExpression() + " -> " + getOutputs() + "]";
    }

    @Override
    public void addRoutes(RouteContext routeContext, Collection<Route> routes) throws Exception {
        Endpoint from = routeContext.getEndpoint();
        final Processor processor = routeContext.createProcessor(this);
        final Aggregator service = new Aggregator(from, processor, getExpression()
            .createExpression(routeContext), aggregationStrategy);

        if (batchSize != 0) {
            service.setBatchSize(batchSize);
        }
        if (batchSize != 0) {
            service.setBatchTimeout(batchTimeout);
        }

        Route route = new Route<Exchange>(from, service) {
            @Override
            public String toString() {
                return "AggregatorRoute[" + getEndpoint() + " -> " + processor + "]";
            }
        };

        routes.add(route);
    }

    public AggregationStrategy getAggregationStrategy() {
        return aggregationStrategy;
    }

    public void setAggregationStrategy(AggregationStrategy aggregationStrategy) {
        this.aggregationStrategy = aggregationStrategy;
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

    public AggregatorType batchSize(int batchSize){
        setBatchSize(batchSize);
        return this;
    }
    
    public AggregatorType batchTimeout(long batchTimeout){
        setBatchTimeout(batchTimeout);
        return this;
    }
}
