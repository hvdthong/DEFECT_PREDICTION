package org.apache.xerces.framework;

import org.xml.sax.Locator;

/**
 * Error handling
 *
 * @version $Id: XMLErrorReporter.java 315404 2000-04-04 21:14:25Z andyc $
 */
public interface XMLErrorReporter {

    /** Warning type. */
    public static final int ERRORTYPE_WARNING = 0;

    /** Error type. */
    public static final int ERRORTYPE_RECOVERABLE_ERROR = 1;

    /** Fatal error type. */
    public static final int ERRORTYPE_FATAL_ERROR = 2;

    /**
     * Get the default locator to use when reporting errors.
     */
    public Locator getLocator();

    /**
     * Report an error detected by a component of the XML parser.
     * In a typical implementation of this interface, this method
     * would call the error handler registered by the user with
     * the appropriate error information.
     *
     * @param locator       Used to determine the location of the error.
     * @param errorDomain   The error domain of the error.
     * @param majorCode     The major key for the message text.
     * @param minorCode     The minor key for the message text.
     * @param args          The arguments to be used as replacement text
     *                      in the message created.
     * @param errorType     The type of error (ERRORTYPE_WARNING, ERRORTYPE_RECOVERABLE_ERROR, ERRORTYPE_FATAL_ERROR).
     *
     * @see #ERRORTYPE_WARNING
     * @see #ERRORTYPE_RECOVERABLE_ERROR
     * @see #ERRORTYPE_FATAL_ERROR
     *
     * @exception Exception Thrown if the parser should not continue
     *                      to the error being handled.
     */
    public void reportError(Locator locator,
                            String errorDomain,
                            int majorCode,
                            int minorCode,
                            Object args[],
                            int errorType) throws Exception;
}
