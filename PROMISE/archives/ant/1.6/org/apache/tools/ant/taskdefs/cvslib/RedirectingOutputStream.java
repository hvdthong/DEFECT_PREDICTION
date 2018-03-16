package org.apache.tools.ant.taskdefs.cvslib;

import org.apache.tools.ant.taskdefs.LogOutputStream;

/**
 * A dummy stream that just passes stuff to the parser.
 *
 */
class RedirectingOutputStream
     extends LogOutputStream {
    private final ChangeLogParser parser;


    /**
     * Creates a new instance of this class.
     *
     * @param parser the parser to which output is sent.
     */
    public RedirectingOutputStream(final ChangeLogParser parser) {
        super(null, 0);
        this.parser = parser;
    }


    /**
     * Logs a line to the log system of ant.
     *
     * @param line the line to log.
     */
    protected void processLine(final String line) {
        parser.stdout(line);
    }
}

