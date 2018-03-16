package org.apache.tools.ant.filters;

import java.io.IOException;
import java.io.Reader;

/**
 * This method converts non-latin characters to unicode escapes.
 * Useful to load properties containing non latin
 * Example:
 *
 * <pre>&lt;escapeunicode&gt;</pre>
 *
 * Or:
 *
 * <pre>&lt;filterreader
        classname=&quot;org.apache.tools.ant.filters.EscapeUnicode&quot;/&gt;
 *  </pre>
 *
 * @since Ant 1.6
 */
public class EscapeUnicode
    extends BaseParamFilterReader
    implements ChainableReader {
    private StringBuffer unicodeBuf;

    /**
     * Constructor for "dummy" instances.
     *
     * @see BaseFilterReader#BaseFilterReader()
     */
    public EscapeUnicode() {
        super();
        unicodeBuf = new StringBuffer();
    }

    /**
     * Creates a new filtered reader.
     *
     * @param in A Reader object providing the underlying stream.
     *           Must not be <code>null</code>.
     */
    public EscapeUnicode(final Reader in) {
        super(in);
        unicodeBuf = new StringBuffer();
    }

    /**
     * Returns the next character in the filtered stream, converting non latin
     * characters to unicode escapes.
     *
     * @return the next character in the resulting stream, or -1
     * if the end of the resulting stream has been reached
     *
     * @exception IOException if the underlying stream throws
     * an IOException during reading
     */
    public final int read() throws IOException {
        if (!getInitialized()) {
            initialize();
            setInitialized(true);
        }

        int ch = -1;
        if (unicodeBuf.length() == 0) {
            ch = in.read();
            if (ch != -1) {
                char achar = (char) ch;
                if (achar >= '\u0080') {
                    unicodeBuf = new StringBuffer("u0000");
                    String s = Integer.toHexString(ch);
                    for (int i = 0; i < s.length(); i++) {
                        unicodeBuf.setCharAt(unicodeBuf.length()
                                             - s.length() + i,
                                             s.charAt(i));
                    }
                    ch = '\\';
                }
            }
        } else {
            ch = (int) unicodeBuf.charAt(0);
            unicodeBuf.deleteCharAt(0);
        }
        return ch;
    }

    /**
     * Creates a new EscapeUnicode using the passed in
     * Reader for instantiation.
     *
     * @param rdr A Reader object providing the underlying stream.
     *            Must not be <code>null</code>.
     *
     * @return a new filter based on this configuration, but filtering
     *         the specified reader
     */
    public final Reader chain(final Reader rdr) {
        EscapeUnicode newFilter = new EscapeUnicode(rdr);
        newFilter.setInitialized(true);
        return newFilter;
    }

    /**
     * Parses the parameters (currently unused)
     */
    private final void initialize() {
    }
}

