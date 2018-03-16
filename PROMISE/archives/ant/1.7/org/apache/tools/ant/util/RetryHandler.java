package org.apache.tools.ant.util;

import java.io.IOException;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

/**
 * A simple utility class to take a piece of code (that implements
 * <code>Retryable</code> interface) and executes that with possibility to
 * retry the execution in case of IOException.
 */
public class RetryHandler {

    private int retriesAllowed = 0;
    private Task task;

    /**
     * Create a new RetryingHandler.
     *
     * @param retriesAllowed how many times to retry
     * @param task the Ant task that is is executed from, used for logging only
     */
    public RetryHandler(int retriesAllowed, Task task) {
        this.retriesAllowed = retriesAllowed;
        this.task = task;
    }

    /**
     * Execute the <code>Retryable</code> code with specified number of retries.
     *
     * @param exe the code to execute
     * @param desc some descriptive text for this piece of code, used for logging
     * @throws IOException if the number of retries has exceeded the allowed limit
     */
    public void execute(Retryable exe, String desc) throws IOException {
        int retries = 0;
        while (true) {
            try {
                exe.execute();
                break;
            } catch (IOException e) {
                retries++;
                if (retries > this.retriesAllowed && this.retriesAllowed > -1) {
                    task.log("try #" + retries + ": IO error ("
                            + desc + "), number of maximum retries reached ("
                            + this.retriesAllowed + "), giving up", Project.MSG_WARN);
                    throw e;
                } else {
                    task.log("try #" + retries + ": IO error (" + desc
                             + "), retrying", Project.MSG_WARN);
                }
            }
        }
    }

}
