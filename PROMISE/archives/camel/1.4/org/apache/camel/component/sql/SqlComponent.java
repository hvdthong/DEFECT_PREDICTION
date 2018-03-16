package org.apache.camel.component.sql;

import java.util.Map;

import javax.sql.DataSource;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

/**
 * @version $Revision:520964 $
 */
public class SqlComponent extends DefaultComponent {

    private DataSource dataSource;

    public SqlComponent() {
    }

    public SqlComponent(CamelContext context) {
        super(context);
    }

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map parameters)
        throws Exception {
        return new SqlEndpoint(uri, remaining.replaceAll("#", "?"), this, dataSource, parameters);
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
