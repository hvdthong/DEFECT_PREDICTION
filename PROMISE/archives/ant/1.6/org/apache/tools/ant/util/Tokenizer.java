package org.apache.tools.ant.util;

import java.io.Reader;
import java.io.IOException;

/**
 * input stream tokenizers implement this interface
 *
 * @version Ant 1.6
 */
public interface Tokenizer {
    /**
     * get the next token from the input stream
     * @param in the input stream
     * @return the next token, or null for the end
     *         of the stream
     * @throws IOException if an error occurs
     */
    String getToken(Reader in)
        throws IOException;

    /**
     * return the string between tokens, after the
     * previous token.
     * @return the intra-token string
     */
    String getPostToken();
}
