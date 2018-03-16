package org.apache.camel.component.jdbc;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;
import org.apache.camel.impl.DefaultExchange;

/**
 * @version $Revision:520964 $
 */
public class JdbcComponent extends DefaultComponent<DefaultExchange> {

    public JdbcComponent() {
    }

    public JdbcComponent(CamelContext context) {
        super(context);
    }

    @Override
    protected Endpoint<DefaultExchange> createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        return new JdbcEndpoint(uri, remaining, this);
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();
    }

    @Override
    protected void doStop() throws Exception {
        super.doStop();
    }

}
