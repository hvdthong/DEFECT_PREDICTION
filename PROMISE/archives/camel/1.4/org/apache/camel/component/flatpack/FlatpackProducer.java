package org.apache.camel.component.flatpack;

import net.sf.flatpack.DataSet;
import net.sf.flatpack.Parser;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;

/**
 * @version $Revision: 1.1 $
 */
class FlatpackProducer extends DefaultProducer<Exchange> {
    private FixedLengthEndpoint endpoint;

    public FlatpackProducer(FixedLengthEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

    public void process(Exchange exchange) throws Exception {
        Parser parser = endpoint.createParser(exchange);
        DataSet dataSet = parser.parse();
        int counter = 0;
        while (dataSet.next()) {
            endpoint.processDataSet(dataSet, counter++);
        }
    }
}
