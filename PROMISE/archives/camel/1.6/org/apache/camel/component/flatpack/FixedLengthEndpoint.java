package org.apache.camel.component.flatpack;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import net.sf.flatpack.DataSet;
import net.sf.flatpack.DefaultParserFactory;
import net.sf.flatpack.Parser;
import net.sf.flatpack.ParserFactory;
import org.apache.camel.Consumer;
import org.apache.camel.Exchange;
import org.apache.camel.InvalidPayloadException;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultPollingEndpoint;
import org.apache.camel.processor.loadbalancer.LoadBalancer;
import org.apache.camel.processor.loadbalancer.LoadBalancerConsumer;
import org.apache.camel.processor.loadbalancer.RoundRobinLoadBalancer;
import org.apache.camel.util.ExchangeHelper;
import org.apache.camel.util.ObjectHelper;
import org.springframework.core.io.Resource;

/**
 * for working with fixed width and delimited files
 *
 * @version $Revision: 686616 $
 */
public class FixedLengthEndpoint extends DefaultPollingEndpoint<Exchange> {
    private final Resource resource;
    private LoadBalancer loadBalancer = new RoundRobinLoadBalancer();
    private ParserFactory parserFactory = DefaultParserFactory.getInstance();
    private boolean splitRows = true;

    public FixedLengthEndpoint(String uri, Resource resource) {
        super(uri);
        this.resource = resource;
    }

    public boolean isSingleton() {
        return true;
    }

    public Producer<Exchange> createProducer() throws Exception {
        return new FlatpackProducer(this);
    }

    public Consumer<Exchange> createConsumer(Processor processor) throws Exception {
        return new LoadBalancerConsumer(this, processor, loadBalancer);
    }

    public void processDataSet(DataSet dataSet, int counter) throws Exception {
        Exchange exchange = createExchange(dataSet, counter);
        loadBalancer.process(exchange);
    }

    public Exchange createExchange(DataSet dataSet, int counter) {
        Exchange answer = createExchange();
        Message in = answer.getIn();
        in.setBody(dataSet);
        in.setHeader("camelFlatpackCounter", counter);
        return answer;
    }

    public Parser createParser(Exchange exchange) throws InvalidPayloadException, IOException {
        Resource resource = getResource();
        ObjectHelper.notNull(resource, "endpoint.resource");
        Reader bodyReader = ExchangeHelper.getMandatoryInBody(exchange, Reader.class);
        return createParser(resource, bodyReader);
    }

    protected Parser createParser(Resource resource, Reader bodyReader) throws IOException {
        return getParserFactory().newFixedLengthParser(new InputStreamReader(resource.getInputStream()), bodyReader);
    }


    public Resource getResource() {
        return resource;
    }

    public ParserFactory getParserFactory() {
        return parserFactory;
    }

    public void setParserFactory(ParserFactory parserFactory) {
        this.parserFactory = parserFactory;
    }

    public LoadBalancer getLoadBalancer() {
        return loadBalancer;
    }

    public void setLoadBalancer(LoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    public boolean isSplitRows() {
        return splitRows;
    }

    public void setSplitRows(boolean splitRows) {
        this.splitRows = splitRows;
    }
}
