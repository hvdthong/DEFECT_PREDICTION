package org.apache.tools.ant.filters;

import java.io.IOException;
import java.io.Reader;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import org.apache.tools.ant.types.Parameter;

/**
 * Concats a file before and/or after the file.
 *
 * <p>Example:<pre>
 * <copy todir="build">
 *     <fileset dir="src" includes="*.java"/>
 *     <filterchain>
 *         <concatfilter prepend="apache-license-java.txt"/>
 *     </filterchain>
 * </copy>
 * </pre>
 * Copies all java sources from <i>src</i> to <i>build</i> and adds the
 * content of <i>apache-license-java.txt</i> add the beginning of each
 * file.</p>
 *
 * @since 1.6
 * @version 2003-09-23
 */
public final class ConcatFilter extends BaseParamFilterReader
    implements ChainableReader {

    /** File to add before the content. */
    private File prepend;

    /** File to add after the content. */
    private File append;

    /** Reader for prepend-file. */
    private Reader prependReader = null;

    /** Reader for append-file. */
    private Reader appendReader = null;

    /**
     * Constructor for "dummy" instances.
     *
     * @see BaseFilterReader#BaseFilterReader()
     */
    public ConcatFilter() {
        super();
    }

    /**
     * Creates a new filtered reader.
     *
     * @param in A Reader object providing the underlying stream.
     *           Must not be <code>null</code>.
     */
    public ConcatFilter(final Reader in) {
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
    public int read() throws IOException {
        if (!getInitialized()) {
            initialize();
            setInitialized(true);
        }

        int ch = -1;

        if (prependReader != null) {
            ch = prependReader.read();
            if (ch == -1) {
                prependReader.close();
                prependReader = null;
            }
        }
        if (ch == -1) {
            ch = super.read();
        }
        if (ch == -1) {
            if (appendReader != null) {
                ch = appendReader.read();
                if (ch == -1) {
                    appendReader.close();
                    appendReader = null;
                }
            }
        }

        return ch;
    }

    /**
     * Sets <i>prepend</i> attribute.
     * @param prepend new value
     */
    public void setPrepend(final File prepend) {
        this.prepend = prepend;
    }

    /**
     * Returns <i>prepend</i> attribute.
     * @return prepend attribute
     */
    public File getPrepend() {
        return prepend;
    }

    /**
     * Sets <i>append</i> attribute.
     * @param append new value
     */
    public void setAppend(final File append) {
        this.append = append;
    }

    /**
     * Returns <i>append</i> attribute.
     * @return append attribute
     */
    public File getAppend() {
        return append;
    }

    /**
     * Creates a new ConcatReader using the passed in
     * Reader for instantiation.
     *
     * @param rdr A Reader object providing the underlying stream.
     *            Must not be <code>null</code>.
     *
     * @return a new filter based on this configuration, but filtering
     *         the specified reader
     */
    public Reader chain(final Reader rdr) {
        ConcatFilter newFilter = new ConcatFilter(rdr);
        newFilter.setPrepend(getPrepend());
        newFilter.setAppend(getAppend());
        return newFilter;
    }

    /**
     * Scans the parameters list for the "lines" parameter and uses
     * it to set the number of lines to be returned in the filtered stream.
     * also scan for skip parameter.
     */
    private void initialize() throws IOException {
        Parameter[] params = getParameters();
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                if ("prepend".equals(params[i].getName())) {
                    setPrepend(new File(params[i].getValue()));
                    continue;
                }
                if ("append".equals(params[i].getName())) {
                    setAppend(new File(params[i].getValue()));
                    continue;
                }
            }
        }
        if (prepend != null) {
            if (!prepend.isAbsolute()) {
                prepend = new File(getProject().getBaseDir(), prepend.getPath());
            }
            prependReader = new BufferedReader(new FileReader(prepend));
        }
        if (append != null) {
            if (!append.isAbsolute()) {
                append = new File(getProject().getBaseDir(), append.getPath());
            }
            appendReader = new BufferedReader(new FileReader(append));
        }
   }
}
