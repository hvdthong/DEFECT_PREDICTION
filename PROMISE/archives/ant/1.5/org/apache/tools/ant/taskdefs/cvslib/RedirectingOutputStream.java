package org.apache.tools.ant.taskdefs.cvslib;

import org.apache.tools.ant.taskdefs.LogOutputStream;

/**
 * A dummy stream that just passes stuff to the parser.
 *
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 * @version $Revision: 274041 $ $Date: 2003-02-10 22:25:38 +0800 (周一, 2003-02-10) $
 */
class RedirectingOutputStream
     extends LogOutputStream {
    private final ChangeLogParser m_parser;


    /**
     * Creates a new instance of this class.
     *
     * @param parser the parser to which output is sent.
     */
    public RedirectingOutputStream(final ChangeLogParser parser) {
        super(null, 0);
        m_parser = parser;
    }


    /**
     * Logs a line to the log system of ant.
     *
     * @param line the line to log.
     */
    protected void processLine(final String line) {
        m_parser.stdout(line);
    }
}

