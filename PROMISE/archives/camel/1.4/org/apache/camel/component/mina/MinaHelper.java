package org.apache.camel.component.mina;

import org.apache.camel.CamelExchangeException;
import org.apache.camel.Exchange;
import org.apache.mina.common.IoSession;
import org.apache.mina.common.WriteFuture;

/**
 * Helper class used internally by camel-mina using Apache MINA.
 */
public final class MinaHelper {

    private MinaHelper() {
    }

    /**
     * Writes the given body to MINA session. Will wait until the body has been written.
     *
     * @param session   the MINA session
     * @param body      the body to write (send)
     * @param exchange  the mina exchange used for error reporting
     * @throws CamelExchangeException is thrown if the body could not be written for some reasons
     *                                (eg remote connection is closed etc.)
     */
    public static void writeBody(IoSession session, Object body, Exchange exchange) throws CamelExchangeException {
        WriteFuture future = session.write(body);
        future.join();
        if (!future.isWritten()) {
            throw new CamelExchangeException("Could not write body", exchange);
        }
    }

}
