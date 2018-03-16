package org.apache.tools.ant.filters;

import java.io.IOException;
import java.io.Reader;

import org.apache.tools.ant.types.Parameter;

/**
 * Reads the first <code>n</code> lines of a stream.
 * (Default is first 10 lines.)
 * <p>
 * Example:
 * <pre>&lt;headfilter lines=&quot;3&quot;/&gt;</pre>
 * Or:
 * <pre>&lt;filterreader classname=&quot;org.apache.tools.ant.filters.HeadFilter&quot;&gt;
 *    &lt;param name=&quot;lines&quot; value=&quot;3&quot;/&gt;
 * &lt;/filterreader&gt;</pre>
 *
 * @author Magesh Umasankar
 */
public final class HeadFilter
    extends BaseParamFilterReader
    implements ChainableReader {
    /** Parameter name for the number of lines to be returned. */
    private static final String LINES_KEY = "lines";

    /** Number of lines currently read in. */
    private long linesRead = 0;

    /** Number of lines to be returned in the filtered stream. */
    private long lines = 10;

    /**
     * Constructor for "dummy" instances.
     * 
     * @see BaseFilterReader#BaseFilterReader()
     */
    public HeadFilter() {
        super();
    }

    /**
     * Creates a new filtered reader.
     *
     * @param in A Reader object providing the underlying stream.
     *           Must not be <code>null</code>.
     */
    public HeadFilter(final Reader in) {
        super(in);
    }

    /**
     * Returns the next character in the filtered stream. If the desired
     * number of lines have already been read, the resulting stream is
     * effectively at an end. Otherwise, the next character from the 
     * underlying stream is read and returned.
     * 
     * @return the next character in the resulting stream, or -1
     * if the end of the resulting stream has been reached
     * 
     * @exception IOException if the underlying stream throws an IOException
     * during reading     
     */
    public final int read() throws IOException {
        if (!getInitialized()) {
            initialize();
            setInitialized(true);
        }

        int ch = -1;

        if (linesRead < lines) {

            ch = in.read();

            if (ch == '\n') {
                linesRead++;
            }
        }

        return ch;
    }

    /**
     * Sets the number of lines to be returned in the filtered stream.
     * 
     * @param lines the number of lines to be returned in the filtered stream
     */
    public final void setLines(final long lines) {
        this.lines = lines;
    }

    /**
     * Returns the number of lines to be returned in the filtered stream.
     * 
     * @return the number of lines to be returned in the filtered stream
     */
    private final long getLines() {
        return lines;
    }

    /**
     * Creates a new HeadFilter using the passed in
     * Reader for instantiation.
     * 
     * @param rdr A Reader object providing the underlying stream.
     *            Must not be <code>null</code>.
     * 
     * @return a new filter based on this configuration, but filtering
     *         the specified reader
     */
    public final Reader chain(final Reader rdr) {
        HeadFilter newFilter = new HeadFilter(rdr);
        newFilter.setLines(getLines());
        newFilter.setInitialized(true);
        return newFilter;
    }

    /**
     * Scans the parameters list for the "lines" parameter and uses
     * it to set the number of lines to be returned in the filtered stream.
     */
    private final void initialize() {
        Parameter[] params = getParameters();
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                if (LINES_KEY.equals(params[i].getName())) {
                    lines = new Long(params[i].getValue()).longValue();
                    break;
                }
            }
        }
    }
}
