package org.apache.camel.util;

import java.io.IOException;

/**
 * IO helper class.
 *
 * @version $Revision: 659836 $
 */
public final class IOHelper {

    private IOHelper() {
    }

    /**
     * A factory method which creates an {@link IOException} from the given
     * exception and message
     */
    public static IOException createIOException(Throwable cause) {
        return createIOException(cause.getMessage(), cause);
    }

    /**
     * A factory method which creates an {@link IOException} from the given
     * exception and message
     */
    public static IOException createIOException(String message, Throwable cause) {
        IOException answer = new IOException(message);
        answer.initCause(cause);
        return answer;
    }
}
