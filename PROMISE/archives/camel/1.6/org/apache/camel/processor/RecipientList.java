package org.apache.camel.processor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.ProducerCache;
import org.apache.camel.impl.ServiceSupport;
import org.apache.camel.processor.aggregate.UseLatestAggregationStrategy;
import org.apache.camel.util.ExchangeHelper;
import org.apache.camel.util.ObjectHelper;
import static org.apache.camel.util.ObjectHelper.notNull;

/**
 * Implements a dynamic <a
 * pattern where the list of actual endpoints to send a message exchange to are
 * dependent on some dynamic expression.
 *
 * @version $Revision: 734353 $
 */
public class RecipientList extends ServiceSupport implements Processor {
    private final Expression<Exchange> expression;
    private ProducerCache<Exchange> producerCache = new ProducerCache<Exchange>();

    public RecipientList(Expression<Exchange> expression) {
        notNull(expression, "expression");
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "RecipientList[" + expression + "]";
    }

    public void process(Exchange exchange) throws Exception {
        Object receipientList = expression.evaluate(exchange);
        sendToRecipientList(exchange, receipientList);
    }

    /**
     * Sends the given exchange to the recipient list
     */
    public void sendToRecipientList(Exchange exchange, Object receipientList) throws Exception {
        Iterator iter = ObjectHelper.createIterator(receipientList);
        List<Processor> processors = new ArrayList<Processor>();
        while (iter.hasNext()) {
            Object recipient = iter.next();
            Endpoint<Exchange> endpoint = resolveEndpoint(exchange, recipient);
            Producer<Exchange> producer = producerCache.getProducer(endpoint);
            processors.add(producer);
        }
        MulticastProcessor mp = new MulticastProcessor(processors, new UseLatestAggregationStrategy());
        mp.process(exchange);
    }

    protected Endpoint<Exchange> resolveEndpoint(Exchange exchange, Object recipient) {
        if (recipient instanceof String) {
            recipient = ((String)recipient).trim();
        }
        return ExchangeHelper.resolveEndpoint(exchange, recipient);
    }

    protected void doStop() throws Exception {
        producerCache.stop();
    }

    protected void doStart() throws Exception {
    }
}
