package org.apache.synapse.mediators.db;

import org.apache.synapse.MessageContext;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;

/**
 * A mediator that writes (i.e. inserts one row) to a table using message information
 */
public class DBReportMediator extends AbstractDBMediator {

    protected void processStatement(Statement stmnt, MessageContext msgCtx) {

        boolean traceOn = isTraceOn(msgCtx);
        boolean traceOrDebugOn = isTraceOrDebugOn(traceOn);

        Connection con = null;
        try {
            PreparedStatement ps = getPreparedStatement(stmnt, msgCtx);
            con = ps.getConnection();
            int count = ps.executeUpdate();

            if (count > 0) {
                if (traceOrDebugOn) {
                    traceOrDebug(traceOn,
                        "Inserted " + count + " row/s using statement : " + stmnt.getRawStatement());
                }
            } else {
                if (traceOrDebugOn) {
                    traceOrDebug(traceOn,
                        "No rows were inserted for statement : " + stmnt.getRawStatement());
                }
            }
            con.commit();

        } catch (SQLException e) {
            handleException("Error execuring insert statement : " + stmnt.getRawStatement() +
                " against DataSource : " + getDSName(), e, msgCtx);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ignore) {}
            }
        }
    }
}
