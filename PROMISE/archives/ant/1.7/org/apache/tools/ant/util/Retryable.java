package org.apache.tools.ant.util;

import java.io.IOException;


/**
 * Simple interface for executing a piece of code. Used for writing anonymous inner
 * classes in FTP task for retry-on-IOException behaviour.
 *
 * @see RetryHandler
 */
public interface Retryable {
    /** The value to use to never give up. */
    int RETRY_FOREVER = -1;
    /**
     * Called to execute the code.
     * @throws IOException if there is a problem.
     */
    void execute() throws IOException;

}
