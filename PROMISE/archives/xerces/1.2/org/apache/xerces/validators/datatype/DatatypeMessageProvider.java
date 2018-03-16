package org.apache.xerces.validators.datatype;

import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.ResourceBundle;
import org.apache.xerces.utils.XMLMessageProvider;

/**
 * 
 * @author Jeffrey Rodriguez
 * @version $Id: DatatypeMessageProvider.java 316045 2000-08-31 19:48:48Z jeffreyr $
 */
public class DatatypeMessageProvider implements XMLMessageProvider {
    /**
     * The domain of messages concerning the XML Schema: Datatypes specification.
     */

    /**
     * Set the locale used for error messages
     *
     * @param locale the new locale
     */
    public void setLocale(Locale locale) {
        fLocale = locale;
    }
    /**
     * get the local used for error messages
     *
     * @return the locale
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
                fResourceBundle = ListResourceBundle.getBundle("org.apache.xerces.msg.DatatypeMessages", locale);
            if (fResourceBundle == null)
                fResourceBundle = ListResourceBundle.getBundle("org.apache.xerces.msg.DatatypeMessages");
        }
        if (majorCode < 0 || majorCode >= fgMessageKeys.length) {
            majorCode = MSG_BAD_MAJORCODE;
            throwex = true;
        }
        String msgKey = fgMessageKeys[majorCode];
        String msg = fResourceBundle.getString(msgKey);
        if (args != null) {
            try {
                msg = java.text.MessageFormat.format(msg, args);
            } catch (Exception e) {
                msg = fResourceBundle.getString(fgMessageKeys[MSG_FORMAT_FAILURE]);
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
        NotBoolean = 2,
        NotDecimal = 3,
        FacetsInconsistent = 4,
        IllegalFacetValue = 5,
        IllegalDecimalFacet = 6,
        UnknownFacet = 7,
        InvalidEnumValue = 8,
        OutOfBounds = 9,
        NotAnEnumValue = 10,
        NotInteger = 11,
        IllegalIntegerFacet = 12,
        NotReal = 13,
        IllegalRealFacet = 14,
        ScaleLargerThanPrecision = 15,
        PrecisionExceeded = 16,
        ScaleExceeded = 17,
        NotFloat = 18,
        MSG_MAX_CODE = 19;

    public static final int
        MSG_NONE = 0;

    public static final String[] fgMessageKeys = {

    };

}
