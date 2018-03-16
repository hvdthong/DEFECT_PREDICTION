package org.apache.camel.component.ibatis;

import com.ibatis.sqlmap.client.SqlMapClient;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.apache.camel.util.ExchangeHelper;

/**
 * @version $Revision: 1.1 $
 */
public class IBatisProducer extends DefaultProducer {
    private SqlMapClient sqlClient;
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
        if (sqlClient == null) {
            sqlClient = endpoint.getSqlClient();
        }
        Object body = ExchangeHelper.getMandatoryInBody(exchange);
        String operation = getOperationName(exchange);
        sqlClient.insert(operation, body);
    }

    /**
     * Returns the iBatis insert operation name
     */
    protected String getOperationName(Exchange exchange) {
        return endpoint.getEntityName();
    }
}
