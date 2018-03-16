package org.apache.camel.component.ibatis;

import java.util.Iterator;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.apache.camel.util.ObjectHelper;

/**
 * @version $Revision: 699285 $
 */
public class IBatisProducer extends DefaultProducer {
    private final IBatisEndpoint endpoint;

    public IBatisProducer(IBatisEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

    @Override
    public IBatisEndpoint getEndpoint() {
        return (IBatisEndpoint) super.getEndpoint();
    }

    public void process(Exchange exchange) throws Exception {
        Object body = exchange.getIn().getBody();
        if (body == null) {
            endpoint.query(exchange.getOut(true));
        } else {
            String operation = getOperationName(exchange);

            Iterator iter = ObjectHelper.createIterator(body);
            while (iter.hasNext()) {
                endpoint.getSqlClient().insert(operation, iter.next());
            }
        }
    }

    /**
     * Returns the iBatis insert operation name
     */
    protected String getOperationName(Exchange exchange) {
        return endpoint.getEntityName();
    }
}
