package org.apache.camel.component.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.NoTypeConversionAvailableException;
import org.apache.camel.impl.DefaultProducer;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;

public class SqlProducer extends DefaultProducer {
    public static final String UPDATE_COUNT = "org.apache.camel.sql.update-count";
    private String query;
    private JdbcTemplate jdbcTemplate;

    public SqlProducer(SqlEndpoint endpoint, String query, JdbcTemplate jdbcTemplate) {
        super(endpoint);
        this.jdbcTemplate = jdbcTemplate;
        this.query = query;
    }

    public void process(final Exchange exchange) throws Exception {
        jdbcTemplate.execute(query, new PreparedStatementCallback() {
            public Object doInPreparedStatement(PreparedStatement ps) throws SQLException,
                DataAccessException {
                int argNumber = 1;
                try {
                    Iterator<?> iterator = exchange.getIn().getBody(Iterator.class);
                    while (iterator != null && iterator.hasNext()) {
                        ps.setObject(argNumber++, iterator.next());
                    }
                } catch (NoTypeConversionAvailableException e) {
                }

                int expected = ps.getParameterMetaData().getParameterCount();
                if (argNumber - 1 != expected) {
                    throw new SQLException("Number of parameters mismatch. Expected: " + expected + ", was:" + (argNumber - 1));
                }
                
                boolean isResultSet = ps.execute();
                
                if (isResultSet) {
                    RowMapperResultSetExtractor mapper = new RowMapperResultSetExtractor(new ColumnMapRowMapper());
                    List<?> result = (List<?>) mapper.extractData(ps.getResultSet());
                    exchange.getOut().setBody(result);
                    exchange.getOut().setHeaders(exchange.getIn().getHeaders());
                } else {
                    exchange.getIn().setHeader(UPDATE_COUNT, ps.getUpdateCount());
                }

                return null;
            }
        });
    }

}
