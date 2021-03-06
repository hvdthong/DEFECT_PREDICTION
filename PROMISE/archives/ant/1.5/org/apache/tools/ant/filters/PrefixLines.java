package org.apache.tools.ant.filters;

import java.io.IOException;
import java.io.Reader;

import org.apache.tools.ant.types.Parameter;

/**
 * Attaches a prefix to every line.
 *
 * Example:
 * <pre>&lt;prefixlines prefix=&quot;Foo&quot;/&gt;</pre>
 *
 * Or:
 *
 * <pre>&lt;filterreader classname=&quot;org.apache.tools.ant.filters.PrefixLines&quot;&gt;
 *  &lt;param name=&quot;prefix&quot; value=&quot;Foo&quot;/&gt;
 * &lt;/filterreader&gt;</pre>
 *
 * @author Magesh Umasankar
 */
public final class PrefixLines
    extends BaseParamFilterReader
    implements ChainableReader {
    /** Parameter name for the prefix. */
    private static final String PREFIX_KEY = "prefix";

    /** The prefix to be used. */
    private String prefix = null;

    /** Data that must be read from, if not null. */
    private String queuedData = null;

    /**
     * Constructor for "dummy" instances.
     * 
     * @see BaseFilterReader#BaseFilterReader()
     */
    public PrefixLines() {
        super();
    }

    /**
     * Creates a new filtered reader.
     *
     * @param in A Reader object providing the underlying stream.
     *           Must not be <code>null</code>.
     */
    public PrefixLines(final Reader in) {
        super(in);
    }

    /**
     * Returns the next character in the filtered stream. One line is read
     * from the original input, and the prefix added. The resulting
     * line is then used until it ends, at which point the next original line
     * is read, etc.
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

        if (queuedData != null && queuedData.length() == 0) {
            queuedData = null;
        }

        if (queuedData != null) {
            ch = queuedData.charAt(0);
            queuedData = queuedData.substring(1);
            if (queuedData.length() == 0) {
                queuedData = null;
            }
        } else {
            queuedData = readLine();
            if (queuedData == null) {
                ch = -1;
            } else {
                if (prefix != null) {
                    queuedData = prefix + queuedData;
                }
                return read();
            }
        }
        return ch;
    }

    /**
     * Sets the prefix to add at the start of each input line.
     * 
     * @param prefix The prefix to add at the start of each input line.
     *               May be <code>null</code>, in which case no prefix
     *               is added.
     */
    public final void setPrefix(final String prefix) {
        this.prefix = prefix;
    }

    /**
     * Returns the prefix which will be added at the start of each input line.
     * 
     * @return the prefix which will be added at the start of each input line
     */
    private final String getPrefix() {
        return prefix;
    }

    /**
     * Creates a new PrefixLines filter using the passed in
     * Reader for instantiation.
     * 
     * @param rdr A Reader object providing the underlying stream.
     *            Must not be <code>null</code>.
     * 
     * @return a new filter based on this configuration, but filtering
     *         the specified reader
     */
    public final Reader chain(final Reader rdr) {
        PrefixLines newFilter = new PrefixLines(rdr);
        newFilter.setPrefix(getPrefix());
        newFilter.setInitialized(true);
        return newFilter;
    }

    /**
     * Initializes the prefix if it is available from the parameters.
     */
    private final void initialize() {
        Parameter[] params = getParameters();
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                if (PREFIX_KEY.equals(params[i].getName())) {
                    prefix = params[i].getValue();
                    break;
                }
            }
        }
    }
}
