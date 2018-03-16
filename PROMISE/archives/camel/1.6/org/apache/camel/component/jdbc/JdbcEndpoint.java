package org.apache.camel.component.jdbc;

import javax.sql.DataSource;

import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.impl.DefaultExchange;

/**
 * @version $Revision:520964 $
 */
public class JdbcEndpoint extends DefaultEndpoint<DefaultExchange> {
    private int readSize;
    private DataSource dataSource;

    public JdbcEndpoint(String endpointUri, Component component, DataSource dataSource) {
        super(endpointUri, component);
        this.dataSource = dataSource;
    }

    public boolean isSingleton() {
        return true;
    }

    public Consumer<DefaultExchange> createConsumer(Processor processor) throws Exception {
        throw new UnsupportedOperationException("Not supported");
    }

    public Producer<DefaultExchange> createProducer() throws Exception {
        return new JdbcProducer(this, dataSource, readSize);
    }

    public void setReadSize(int readSize) {
        this.readSize = readSize;
    }

}
