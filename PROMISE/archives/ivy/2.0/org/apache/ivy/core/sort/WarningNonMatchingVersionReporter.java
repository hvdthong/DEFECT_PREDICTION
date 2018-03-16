package org.apache.ivy.core.sort;

import org.apache.ivy.util.Message;

/**
 * A NonMatchingVersionReporter that raise warnings.
 */
public class WarningNonMatchingVersionReporter extends MessageBasedNonMatchingVersionReporter
        implements NonMatchingVersionReporter {

    protected void reportMessage(String msg) {
        Message.warn(msg);
    }

}
