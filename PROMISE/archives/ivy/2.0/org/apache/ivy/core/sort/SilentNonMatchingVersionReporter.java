package org.apache.ivy.core.sort;

import org.apache.ivy.util.Message;

/**
 * A NonMatchingVersionReporter that only print debug message.
 */
public class SilentNonMatchingVersionReporter extends MessageBasedNonMatchingVersionReporter
        implements NonMatchingVersionReporter {

    protected void reportMessage(String msg) {
        Message.debug(msg);
    }

}
