package org.apache.synapse.mediators.db;

import org.apache.synapse.MessageContext;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * A mediator that writes (i.e. inserts one row) to a table using message information
 */
public class DBReportMediator extends AbstractDBMediator {

    protected void processStatement(Statement stmnt, MessageContext msgCtx) {

        boolean traceOn = isTraceOn(msgCtx);
        boolean traceOrDebugOn = isTraceOrDebugOn(traceOn);

        try {
            PreparedStatement ps = getPreparedStatement(stmnt, msgCtx);
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
        } catch (SQLException e) {
            handleException("Error execuring insert statement : " + stmnt.getRawStatement() +
                " against DataSource : " + getDSName(), e, msgCtx);
        }
    }
}
