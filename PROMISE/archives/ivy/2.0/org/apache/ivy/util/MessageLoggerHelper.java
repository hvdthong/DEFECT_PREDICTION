package org.apache.ivy.util;

import java.util.Iterator;
import java.util.List;

public final class MessageLoggerHelper {
    public static void sumupProblems(MessageLogger logger) {
        List myProblems = logger.getProblems();
        if (myProblems.size() > 0) {
            List myWarns = logger.getWarns();
            List myErrors = logger.getErrors();
            if (!myErrors.isEmpty()) {
                logger.log(":: problems summary ::", Message.MSG_ERR);
            } else {
                logger.log(":: problems summary ::", Message.MSG_WARN);
            }
            if (myWarns.size() > 0) {
                logger.log(":::: WARNINGS", Message.MSG_WARN);
                for (Iterator iter = myWarns.iterator(); iter.hasNext();) {
                    String msg = (String) iter.next();
                    logger.log("\t" + msg + "\n", Message.MSG_WARN);
                }
            }
            if (myErrors.size() > 0) {
                logger.log(":::: ERRORS", Message.MSG_ERR);
                for (Iterator iter = myErrors.iterator(); iter.hasNext();) {
                    String msg = (String) iter.next();
                    logger.log("\t" + msg + "\n", Message.MSG_ERR);
                }
            }
            logger.info("\n:: USE VERBOSE OR DEBUG MESSAGE LEVEL FOR MORE DETAILS");
        }
    }

    private MessageLoggerHelper() {
    }
}
