package org.apache.camel.component.sql;

import java.util.Map;

import javax.sql.DataSource;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultComponent;

/**
 * @version $Revision:520964 $
 */
public class SqlComponent extends DefaultComponent<Exchange> {
    private DataSource dataSource;

    public SqlComponent() {
    }

    public SqlComponent(CamelContext context) {
        super(context);
    }

    @Override
    protected Endpoint<Exchange> createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        String dataSourceRef = getAndRemoveParameter(parameters, "dataSourceRef", String.class);
        if (dataSourceRef != null) {
            dataSource = getCamelContext().getRegistry().lookup(dataSourceRef, DataSource.class);
            if (dataSource == null) {
                throw new IllegalArgumentException("DataSource " + dataSourceRef + " not found in registry");
            }
        }
        
        return new SqlEndpoint(uri, remaining.replaceAll("#", "?"), this, dataSource, parameters);
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

}
