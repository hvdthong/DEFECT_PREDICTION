package org.apache.camel.component.ibatis;

import java.sql.SQLException;
import java.util.List;

import com.ibatis.sqlmap.client.SqlMapClient;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.impl.PollingConsumerSupport;

/**
 * @version $Revision: 1.1 $
 */
public class IBatisPollingConsumer extends PollingConsumerSupport {
    private final IBatisEndpoint endpoint;
    private SqlMapClient sqlClient;
    private String queryName;

    public IBatisPollingConsumer(IBatisEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
        queryName = endpoint.getEntityName();
    }

    public Exchange receive(long timeout) {
        return receiveNoWait();
    }

    public Exchange receive() {
        return receiveNoWait();
    }

    public Exchange receiveNoWait() {
        try {
            if (sqlClient == null) {
                sqlClient = endpoint.getSqlClient();
            }
            List list = sqlClient.queryForList(queryName);
            Exchange exchange = endpoint.createExchange();
            Message in = exchange.getIn();
            in.setBody(list);
            in.setHeader("org.apache.camel.ibatis.queryName", queryName);
            return exchange;
        }
        catch (Exception e) {
            throw new RuntimeCamelException("Failed to poll: " + endpoint + ". Reason: " + e, e);
        }
    }

    protected void doStart() throws Exception {
    }

    protected void doStop() throws Exception {
    }
}
