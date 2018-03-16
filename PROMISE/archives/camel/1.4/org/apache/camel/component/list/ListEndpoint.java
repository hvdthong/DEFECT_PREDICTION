package org.apache.camel.component.list;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.camel.CamelContext;
import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.impl.DefaultProducer;
import org.apache.camel.processor.loadbalancer.LoadBalancerConsumer;
import org.apache.camel.processor.loadbalancer.TopicLoadBalancer;
import org.apache.camel.spi.BrowsableEndpoint;

/**
 * An endpoint which maintains a {@link List} of {@link Exchange} instances
 * which can be useful for tooling, debugging and visualising routes.
 *
 * @version $Revision: 659782 $
 */
public class ListEndpoint extends DefaultEndpoint<Exchange> implements BrowsableEndpoint<Exchange> {
    private List<Exchange> exchanges;
    private TopicLoadBalancer loadBalancer = new TopicLoadBalancer();
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public ListEndpoint(String uri, CamelContext camelContext) {
        super(uri, camelContext);
        reset();
    }

    public ListEndpoint(String uri, Component component) {
        super(uri, component);
        reset();
    }

    public ListEndpoint(String endpointUri) {
        super(endpointUri);
        reset();
    }

    public boolean isSingleton() {
        return true;
    }

    public List<Exchange> getExchanges() {
        return exchanges;
    }

    public TopicLoadBalancer getLoadBalancer() {
        return loadBalancer;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    public Producer<Exchange> createProducer() throws Exception {
        return new DefaultProducer<Exchange>(this) {
            public void process(Exchange exchange) throws Exception {
                onExchange(exchange);
            }
        };
    }

    public Consumer<Exchange> createConsumer(Processor processor) throws Exception {
        return new LoadBalancerConsumer(this, processor, loadBalancer);
    }

    public void reset() {
        exchanges = createExchangeList();
    }

    protected List<Exchange> createExchangeList() {
        return new CopyOnWriteArrayList<Exchange>();
    }

    /**
     * Invoked on a message exchange being sent by a producer
     */
    protected void onExchange(Exchange exchange) throws Exception {
        exchanges.add(exchange);

        loadBalancer.process(exchange);
    }
}
