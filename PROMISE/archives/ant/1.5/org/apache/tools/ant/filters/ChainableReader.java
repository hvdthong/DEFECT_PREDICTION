package org.apache.tools.ant.filters;

import java.io.Reader;

/**
 * Interface indicating that a reader may be chained to another one.
 *
 * @author Magesh Umasankar
 */
public interface ChainableReader {
    /**
     * Returns a reader with the same configuration as this one,
     * but filtering input from the specified reader.
     * 
     * @param rdr the reader which the returned reader should be filtering
     * 
     * @return a reader with the same configuration as this one, but
     *         filtering input from the specified reader
     */
    public Reader chain(Reader rdr);
}
