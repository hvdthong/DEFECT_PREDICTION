package org.apache.tools.mail;

import java.io.IOException;

/**
 * Specialized IOException that get thrown if SMPT's QUIT command fails.
 *
 * <p>This seems to happen with some version of MS Exchange that
 * doesn't respond with a 221 code immediately.  See <a
 * report 5273</a>.</p>
 *
 */
public class ErrorInQuitException extends IOException {

    /**
     * Initialise from an IOException
     *
     * @param e the IO Exception.
     */
    public ErrorInQuitException(IOException e) {
        super(e.getMessage());
    }

}
