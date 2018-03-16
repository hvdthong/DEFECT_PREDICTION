package org.apache.tools.ant.util;

import java.io.IOException;
import java.io.Reader;
import org.apache.tools.ant.ProjectComponent;

/**
 * Class to read the complete input into a string.
 * @since Ant 1.7
 */
public class FileTokenizer extends ProjectComponent implements Tokenizer {

    /**
     * Get the complete input as a string
     * @param in the reader object
     * @return the complete input
     * @throws IOException if error reading
     */
    public String getToken(Reader in) throws IOException {
        return FileUtils.readFully(in);
    }

    /**
     * Return the intra-token string
     * @return an empty string always
     */
    public String getPostToken() {
        return "";
    }
}
