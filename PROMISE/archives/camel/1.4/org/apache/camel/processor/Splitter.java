package org.apache.camel.processor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.converter.ObjectConverter;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.apache.camel.util.CollectionHelper;

import static org.apache.camel.util.ObjectHelper.notNull;
/**
 * Implements a dynamic <a
 * where an expression is evaluated to iterate through each of the parts of a
 * message and then each part is then send to some endpoint.
 *
 * @version $Revision: 645359 $
 */
public class Splitter extends MulticastProcessor implements Processor {
    public static final String SPLIT_SIZE = "org.apache.camel.splitSize";
    public static final String SPLIT_COUNTER = "org.apache.camel.splitCounter";

    private final Expression expression;

    public Splitter(Expression expression, Processor destination, AggregationStrategy aggregationStrategy) {
        this(expression, destination, aggregationStrategy, false, null);
    }

    public Splitter(Expression expression, Processor destination,
            AggregationStrategy aggregationStrategy,
            boolean parallelProcessing, ThreadPoolExecutor threadPoolExecutor) {
        super(Collections.singleton(destination), aggregationStrategy, parallelProcessing, threadPoolExecutor);

        this.expression = expression;
        notNull(expression, "expression");
        notNull(destination, "destination");
    }

    @Override
    public String toString() {
        return "Splitter[on: " + expression + " to: " + getProcessors().iterator().next() + " aggregate: " + getAggregationStrategy() + "]";
    }

    @Override
    protected List<ProcessorExchangePair> createProcessorExchangePairs(
        Exchange exchange) {
        Object value = expression.evaluate(exchange);
        Integer collectionSize = CollectionHelper.size(value);
        List<ProcessorExchangePair> result;
        if (collectionSize != null) {
            result = new ArrayList<ProcessorExchangePair>(collectionSize);
        } else {
            result = new ArrayList<ProcessorExchangePair>();
        }
        Iterator<Object> iter = ObjectConverter.iterator(value);
        while (iter.hasNext()) {
            Object part = iter.next();
            Exchange newExchange = exchange.copy();
            Message in = newExchange.getIn();
            in.setBody(part);
            result.add(new ProcessorExchangePair(getProcessors().iterator().next(), newExchange));
        }
        return result;
    }

    @Override
    protected void updateNewExchange(Exchange exchange, int i, List<ProcessorExchangePair> allPairs) {
        exchange.getIn().setHeader(SPLIT_COUNTER, i);
        exchange.getIn().setHeader(SPLIT_SIZE, allPairs.size());
    }
}
