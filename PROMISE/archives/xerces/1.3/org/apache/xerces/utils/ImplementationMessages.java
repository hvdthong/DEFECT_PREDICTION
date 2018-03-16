package org.apache.xerces.utils;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.ListResourceBundle;

/**
 * ImplementationMessages provides messages internal to the parser implementation
 *
 * @version
 */
public class ImplementationMessages implements XMLMessageProvider {
    /**
     * The domain of messages concerning the Xerces implementation.
     */

    /**
     *
     */
    public void setLocale(Locale locale) {
        fLocale = locale;
    }
    /**
     *
     */
    public Locale getLocale() {
        return fLocale;
    }

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
    public String createMessage(Locale locale, int majorCode, int minorCode, Object args[]) {
        boolean throwex = false;
        if (fResourceBundle == null || locale != fLocale) {
            if (locale != null)
                fResourceBundle = ListResourceBundle.getBundle("org.apache.xerces.msg.ImplementationMessages", locale);
            if (fResourceBundle == null)
                fResourceBundle = ListResourceBundle.getBundle("org.apache.xerces.msg.ImplementationMessages");
        }
        if (majorCode < 0 || majorCode >= fgMessageKeys.length - 1) {
            majorCode = BAD_MAJORCODE;
            throwex = true;
        }
        String msgKey = fgMessageKeys[majorCode];
        String msg = fResourceBundle.getString(msgKey);
        if (args != null) {
            try {
                msg = java.text.MessageFormat.format(msg, args);
            } catch (Exception e) {
                msg = fResourceBundle.getString(fgMessageKeys[FORMAT_FAILED]);
                msg += " " + fResourceBundle.getString(msgKey);
            }
        }
        if (throwex) {
            throw new RuntimeException(msg);
        }
        return msg;
    }
    private Locale fLocale = null;
    private ResourceBundle fResourceBundle = null;
    public static final int


    private static final String[] fgMessageKeys = {

        null
    };
}
