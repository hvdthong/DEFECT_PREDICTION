package org.apache.tools.ant.filters;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * Assembles the constants declared in a Java class in
 * <code>key1=value1(line separator)key2=value2</code>
 * format.
 *<p>
 * Notes:
 * <ol>
 * <li>This filter uses the BCEL external toolkit.
 * <li>This assembles only those constants that are not created
 * using the syntax <code>new whatever()</code>
 * <li>This assembles constants declared using the basic datatypes
 * and String only.</li>
 * <li>The access modifiers of the declared constants do not matter.</li>
 *</ol>
 * Example:<br>
 * <pre>&lt;classconstants/&gt;</pre>
 * Or:
 * <pre>&lt;filterreader classname=&quot;org.apache.tools.ant.filters.ClassConstants&quot;/&gt;</pre>
 * @author Magesh Umasankar
 */
public final class ClassConstants
    extends BaseFilterReader
    implements ChainableReader {
    /** Data that must be read from, if not null. */
    private String queuedData = null;

    /** Helper Class to be invoked via reflection. */
    private static final String JAVA_CLASS_HELPER =
        "org.apache.tools.ant.filters.util.JavaClassHelper";

    /**
     * Constructor for "dummy" instances.
     * 
     * @see BaseFilterReader#BaseFilterReader()
     */
    public ClassConstants() {
        super();
    }

    /**
     * Creates a new filtered reader. The contents of the passed-in reader
     * are expected to be the name of the class from which to produce a 
     * list of constants.
     *
     * @param in A Reader object providing the underlying stream.
     *           Must not be <code>null</code>.
     */
    public ClassConstants(final Reader in) {
        super(in);
    }

    /**
     * Reads and assembles the constants declared in a class file.
     * 
     * @return the next character in the list of constants, or -1
     * if the end of the resulting stream has been reached
     * 
     * @exception IOException if the underlying stream throws an IOException
     * during reading, or if the constants for the specified class cannot
     * be read (for example due to the class not being found).
     */
    public final int read() throws IOException {

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
            final String clazz = readFully();
            if (clazz == null) {
                ch = -1;
            } else {
                final byte[] bytes = clazz.getBytes();
                try {
                    final Class javaClassHelper =
                        Class.forName(JAVA_CLASS_HELPER);
                    if (javaClassHelper != null) {
                        final Class params[] = {
                            byte[].class
                        };
                        final Method getConstants =
                            javaClassHelper.getMethod("getConstants", params);
                        final Object[] args = {
                            bytes
                        };
                        final StringBuffer sb = (StringBuffer)
                                getConstants.invoke(null, args);
                        if (sb.length() > 0) {
                            queuedData = sb.toString();
                            return read();
                        }
                    }
                } catch (ClassNotFoundException cnfe) {
                    throw new IOException(cnfe.getMessage());
                } catch (NoSuchMethodException nsme) {
                    throw new IOException(nsme.getMessage());
                } catch (IllegalAccessException iae) {
                    throw new IOException(iae.getMessage());
                } catch (IllegalArgumentException iarge) {
                    throw new IOException(iarge.getMessage());
                } catch (InvocationTargetException ite) {
                    throw new IOException(ite.getMessage());
                }
            }
        }
        return ch;
    }

    /**
     * Creates a new ClassConstants using the passed in
     * Reader for instantiation.
     * 
     * @param rdr A Reader object providing the underlying stream.
     *            Must not be <code>null</code>.
     * 
     * @return a new filter based on this configuration, but filtering
     *         the specified reader
     */
    public final Reader chain(final Reader rdr) {
        ClassConstants newFilter = new ClassConstants(rdr);
        return newFilter;
    }
}
