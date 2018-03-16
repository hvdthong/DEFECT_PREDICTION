package org.apache.tools.mail;

import java.io.IOException;

/**
 * Specialized IOException that get thrown if SMPT's QUIT command fails.
 *
 * <p>This seems to happen with some version of MS Exchange that
 * doesn't respond with a 221 code immediately.  See <a
 * report 5273</a>.</p>
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a> 
 * @version $Revision: 274041 $
 */
public class ErrorInQuitException extends IOException {

    public ErrorInQuitException(IOException e) {
        super(e.getMessage());
    }

}
