package org.apache.tools.ant.util;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Class that can be used to wrap <tt>System.out</tt> and <tt>System.err</tt>
 * without getting anxious about any client closing the stream.
 *
 * <p>
 * In code-language it means that it is not necessary to do:
 * <pre>
 * if (out != System.out && out!= System.err){
 *   out.close();
 * }
 * </pre>
 * </p>
 *
 * @author <a href="mailto:sbailliez@apache.org">Stephane Bailliez</a>
 */
public class KeepAliveOutputStream extends FilterOutputStream {

    public KeepAliveOutputStream(OutputStream out) {
        super(out);
    }

    /** this method does nothing */
    public void close() throws IOException {
    }
}

