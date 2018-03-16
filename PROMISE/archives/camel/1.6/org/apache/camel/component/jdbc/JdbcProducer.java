package org.apache.camel.component.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.impl.DefaultProducer;
import org.apache.camel.util.IntrospectionSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @version $Revision: 713480 $
 */
public class JdbcProducer extends DefaultProducer<DefaultExchange> {
    private static final transient Log LOG = LogFactory.getLog(JdbcProducer.class);
    private DataSource dataSource;
    private int readSize;

    public JdbcProducer(JdbcEndpoint endpoint, DataSource dataSource, int readSize) throws Exception {
        super(endpoint);
        this.dataSource = dataSource;
        this.readSize = readSize;
    }

    /**
     * Execute sql of exchange and set results on output
     */
    public void process(Exchange exchange) throws Exception {
        String sql = exchange.getIn().getBody(String.class);
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = dataSource.getConnection();
            stmt = conn.createStatement();
            if (LOG.isDebugEnabled()) {
                LOG.debug("Executing JDBC statement: " + sql);
            }
            if (stmt.execute(sql)) {
                rs = stmt.getResultSet();
                setResultSet(exchange, rs);
            } else {
                int updateCount = stmt.getUpdateCount();
                exchange.getOut().setHeader("jdbc.updateCount", updateCount);
            }
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                LOG.warn("Error closing JDBC resource: " + e, e);
            }
        }
    }

    /**
     * Sets the result from the ResultSet to the Exchange as its OUT body.
     */
    protected void setResultSet(Exchange exchange, ResultSet rs) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();

        HashMap<String, Object> props = new HashMap<String, Object>();
        IntrospectionSupport.getProperties(meta, props, "jdbc.");
        exchange.getOut().setHeaders(props);

        int count = meta.getColumnCount();
        List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
        int rowNumber = 0;
        while (rs.next() && (readSize == 0 || rowNumber < readSize)) {
            HashMap<String, Object> row = new HashMap<String, Object>();
            for (int i = 0; i < count; i++) {
                int columnNumber = i + 1;
                String columnName = meta.getColumnName(columnNumber);
                row.put(columnName, rs.getObject(columnName));
            }
            data.add(row);
            rowNumber++;
        }

        exchange.getOut().setBody(data);
    }

}
