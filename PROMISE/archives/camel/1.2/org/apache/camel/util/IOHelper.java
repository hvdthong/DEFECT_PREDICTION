package org.apache.camel.util;

import java.io.IOException;

/**
 * @version $Revision: 1.1 $
 */
public class IOHelper {

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
