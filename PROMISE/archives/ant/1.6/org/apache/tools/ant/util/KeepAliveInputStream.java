package org.apache.tools.ant.util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Class that can be used to wrap <tt>System.in</tt>
 * without getting anxious about any client closing the stream.
 *
 * <p>
 * In code-language it means that it is not necessary to do:
 * <pre>
 * if (out != System.in) {
 *   in.close();
 * }
 * </pre>
 * </p>
 *
 * @since Ant 1.6
 */
public class KeepAliveInputStream extends FilterInputStream {

    public KeepAliveInputStream(InputStream in) {
        super(in);
    }

    /** this method does nothing */
    public void close() throws IOException {
    }
}

