package org.apache.camel.component.jdbc;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.ExchangePattern;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.impl.DefaultExchange;

/**
 * @version $Revision:520964 $
 */
public class JdbcEndpoint extends DefaultEndpoint<DefaultExchange> {

    private URI uri;
    private String remaining;
    /** The maximum size for reading a result set <code>readSize</code> */
    private int readSize = 20000;

    protected JdbcEndpoint(String endpointUri, String remaining, JdbcComponent component) throws URISyntaxException {
        super(endpointUri, component);
        this.uri = new URI(endpointUri);
        this.remaining = remaining;
    }

    public boolean isSingleton() {
        return false;
    }

    public Consumer<DefaultExchange> createConsumer(Processor processor) throws Exception {
        throw new RuntimeCamelException("A JDBC Consumer would be the server side of database! No such support here");
    }

    public Producer<DefaultExchange> createProducer() throws Exception {
        return new JdbcProducer(this, remaining, readSize);
    }

    public String getName() {
        String path = uri.getPath();
        if (path == null) {
            path = uri.getSchemeSpecificPart();
        }
        return path;
    }

    public int getReadSize() {
        return this.readSize;
    }

    public void setReadSize(int readSize) {
        this.readSize = readSize;
    }

}
