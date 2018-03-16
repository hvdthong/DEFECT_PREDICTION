package org.apache.xerces.tree;

import java.util.Locale;

import org.xml.sax.*;

/**
 * This interface is used during parsing of XML content to encapsulate
 * information about the location of the parsing event being reported.
 * It is used to report diagnostics for application level problems that
 * relate to the data which was successfuly parsed, and to resolve
 * relative URIs.
 *
 * @author David Brownell
 * @version $Revision: 315388 $
 */
public interface ParseContext
{
    /**
     * Returns an error handler that may be used to report problems
     * of various types:  warnings, recoverable errors, fatal errors.
     */
    ErrorHandler getErrorHandler ();

    /**
     * Returns the preferred locale for diagnostic messsages to be
     * reported through the error handler.  If this locale is not
     * supported by the appropriate message catalog, it is suggested
     * that messages include some sort of diagnostic identifier which
     * is the same in all locales.
     */
    Locale getLocale ();

    /**
     * Returns a locator object which reports where the parse event
     * was detected.  The <em>systemId</em> is the base URI to be used
     * when resolving relative URIs, and other information is used
     * in diagnostics.
     */
    Locator getLocator ();
}
