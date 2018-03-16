package org.apache.tools.ant.taskdefs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Used by <code>Execute</code> to handle input and output stream of
 * subprocesses.
 *
 * @since Ant 1.2
 */
public interface ExecuteStreamHandler {

    /**
     * Install a handler for the input stream of the subprocess.
     *
     * @param os output stream to write to the standard input stream of the
     *           subprocess
     * @throws IOException on error
     */
    void setProcessInputStream(OutputStream os) throws IOException;

    /**
     * Install a handler for the error stream of the subprocess.
     *
     * @param is input stream to read from the error stream from the subprocess
     * @throws IOException on error
     */
    void setProcessErrorStream(InputStream is) throws IOException;

    /**
     * Install a handler for the output stream of the subprocess.
     *
     * @param is input stream to read from the error stream from the subprocess
     * @throws IOException on error
     */
    void setProcessOutputStream(InputStream is) throws IOException;

    /**
     * Start handling of the streams.
     * @throws IOException on error
     */
    void start() throws IOException;

    /**
     * Stop handling of the streams - will not be restarted.
     */
    void stop();
}
