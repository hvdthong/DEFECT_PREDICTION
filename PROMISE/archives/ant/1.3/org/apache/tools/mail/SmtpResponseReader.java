package org.apache.tools.mail;

import java.io.*;

/**
 * A wrapper around the raw input from the SMTP server that assembles
 * multi line responses into a single String.
 *
 * <p>The same rules used here would apply to FTP and other Telnet
 * based protocols as well.</p>
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a> 
 */
public class SmtpResponseReader {

    protected BufferedReader reader = null;
    private StringBuffer result = new StringBuffer();

    /**
     * Wrap this input stream.
     */
    public SmtpResponseReader(InputStream in) {
        reader = new BufferedReader(new InputStreamReader(in)); 
    }

    /**
     * Read until the server indicates that the response is complete.
     *
     * @return Responsecode (3 digits) + Blank + Text from all
     *         response line concatenated (with blanks replacing the \r\n
     *         sequences).
     */
    public String getResponse() throws IOException {
        result.setLength(0);
        String line = reader.readLine();
        if (line != null) {
            result.append(line.substring(0, 3));
            result.append(" ");
        }
        
        while (line != null) {
            append(line);
            if (!hasMoreLines(line)) {
                break;
            }
            line = reader.readLine();
        }
        return result.toString().trim();
    }

    /**
     * Closes the underlying stream.
     */
    public void close() throws IOException {
        reader.close();
    }

    /**
     * Should we expect more input?
     */
    protected boolean hasMoreLines(String line) {
        return line.charAt(3) == '-';
    }

    /**
     * Append the text from this line of the resonse.
     */
    private void append(String line) {
        result.append(line.substring(4));
        result.append(" ");
    }
}
