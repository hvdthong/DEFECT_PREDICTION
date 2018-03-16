package org.apache.camel.component.sql;

import java.util.Map;

import javax.sql.DataSource;

import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.util.IntrospectionSupport;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * SQL Endpoint. Endpoint URI should contain valid SQL statement, but instead of
 * question marks (that are parameter placeholders), sharp signs should be used.
 * This is because in camel question mark has other meaning.
 *
 * @author romkal
 */
public class SqlEndpoint extends DefaultEndpoint {

    private JdbcTemplate jdbcTemplate;

    private String query;

    public SqlEndpoint(String uri, String query, Component component, DataSource dataSource, Map parameters) throws Exception {
        super(uri, component);
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        IntrospectionSupport.setProperties(jdbcTemplate, parameters, "template.");
        this.query = query;
    }

    public SqlEndpoint(String endpointUri, JdbcTemplate jdbcTemplate, String query) {
        super(endpointUri);
        this.jdbcTemplate = jdbcTemplate;
        this.query = query;
    }
    
    public Consumer createConsumer(Processor processor) throws Exception {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Producer createProducer() throws Exception {
        return new SqlProducer(this, query, jdbcTemplate);
    }

    public boolean isSingleton() {
        return false;
    }

}
