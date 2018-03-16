package org.apache.synapse.mediators.db;

import org.apache.synapse.MessageContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

/**
 * Simple database table lookup mediator. Designed only for read/lookup
 */
public class DBLookupMediator extends AbstractDBMediator {

    protected void processStatement(Statement stmnt, MessageContext msgCtx) {

        boolean traceOn = isTraceOn(msgCtx);
        boolean traceOrDebugOn = isTraceOrDebugOn(traceOn);

        try {
            PreparedStatement ps = getPreparedStatement(stmnt, msgCtx);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                if (traceOrDebugOn) {
                    traceOrDebug(traceOn,
                        "Processing the first row returned : " + stmnt.getRawStatement());
                }

                Iterator propNameIter = stmnt.getResultsMap().keySet().iterator();
                while (propNameIter.hasNext()) {

                    String propName = (String) propNameIter.next();
                    String columnStr = (String) stmnt.getResultsMap().get(propName);

                    Object obj = null;
                    try {
                        int colNum = Integer.parseInt(columnStr);
                        obj = rs.getObject(colNum);
                    } catch (NumberFormatException ignore) {
                        obj = rs.getObject(columnStr);
                    }

                    if (obj != null) {
                        if (traceOrDebugOn) {
                            traceOrDebug(traceOn, "Column : " + columnStr +
                                " returned value : " + obj +
                                " Setting this as the message property : " + propName);
                        }
                        msgCtx.setProperty(propName, obj.toString());
                    } else {
                        if (traceOrDebugOn) {
                            traceOrDebugWarn(traceOn, "Column : " + columnStr +
                                " returned null Skip setting message property : " + propName);
                        }
                    }
                }
            } else {
                if (traceOrDebugOn) {
                    traceOrDebug(traceOn, "Statement : "
                        + stmnt.getRawStatement() + " returned 0 rows");
                }
            }
            
        } catch (SQLException e) {
            handleException("Error executing statement : " + stmnt.getRawStatement() +
                " against DataSource : " + getDSName(), e, msgCtx);
        }
    }

}
