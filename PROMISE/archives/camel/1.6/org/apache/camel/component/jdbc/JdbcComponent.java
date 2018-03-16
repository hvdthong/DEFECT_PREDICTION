package org.apache.camel.component.jdbc;

import java.util.Map;
import javax.sql.DataSource;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;
import org.apache.camel.impl.DefaultExchange;

/**
 * @version $Revision:520964 $
 */
public class JdbcComponent extends DefaultComponent<DefaultExchange> {
    private DataSource ds;

    public JdbcComponent() {
    }

    public JdbcComponent(CamelContext context) {
        super(context);
    }

    @Override
    protected Endpoint<DefaultExchange> createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        DataSource dataSource;

        if (ds != null) {
            dataSource = ds;
        } else {
            dataSource = getCamelContext().getRegistry().lookup(remaining, DataSource.class);
            if (dataSource == null) {
                throw new IllegalArgumentException("DataSource " + remaining + " not found in registry");
            }
        }

        JdbcEndpoint jdbc = new JdbcEndpoint(uri, this, dataSource);
        setProperties(jdbc, parameters);
        return jdbc;
    }

    public void setDataSource(DataSource dataSource) {
        this.ds = dataSource;
    }

}
