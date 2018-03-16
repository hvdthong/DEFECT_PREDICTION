package org.apache.xerces.utils;

import java.util.Locale;

/**
 * Interface describing how to provide localized error messages to the XMLErrorReporter
 *
 * @see org.apache.xerces.framework.XMLErrorReporter
 */
public interface XMLMessageProvider {
    /**
     * Set the locale to be used for error messages
     *
     * @param locale the new locale
     */
    public void setLocale(Locale locale);
    /**
     * Get the locale being used for error messages
     *
     * @return the locale
     */
    public Locale getLocale();
    /**
     * Creates a message from the specified key and replacement
     * arguments, localized to the given locale.
     *
     * @param locale    The requested locale of the message to be
     *                  created.
     * @param key       The key for the message text.
     * @param args      The arguments to be used as replacement text
     *                  in the message created.
     */
    public String createMessage(Locale locale, int majorCode, int minorCode, Object args[]);
};
