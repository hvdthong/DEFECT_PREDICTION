package org.apache.xerces.validators.datatype;

import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.ResourceBundle;
import org.apache.xerces.utils.XMLMessageProvider;

/**
 *
 * @author Jeffrey Rodriguez
 * @version $Id: DatatypeMessageProvider.java 317377 2001-07-19 22:37:46Z sandygao $
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
        if ( fResourceBundle == null || locale != fLocale ) {
            if ( locale != null )
                fResourceBundle = ListResourceBundle.getBundle("org.apache.xerces.msg.DatatypeMessages", locale);
            if ( fResourceBundle == null )
                fResourceBundle = ListResourceBundle.getBundle("org.apache.xerces.msg.DatatypeMessages");
        }
        if ( majorCode < 0 || majorCode >= fgMessageKeys.length ) {
            majorCode = MSG_BAD_MAJORCODE;
            throwex = true;
        }
        String msgKey = fgMessageKeys[majorCode];
        String msg = fResourceBundle.getString(msgKey);
        if ( args != null ) {
            try {
                msg = java.text.MessageFormat.format(msg, args);
            }
            catch ( Exception e ) {
                msg = fResourceBundle.getString(fgMessageKeys[MSG_FORMAT_FAILURE]);
                msg += " " + fResourceBundle.getString(msgKey);
            }
        }

        if ( throwex ) {
            throw new RuntimeException(msg);
        }
        return msg;
    }
    private Locale fLocale = null;
    private ResourceBundle fResourceBundle = null;
    private static int counter = 0;
    public static final int
    NOT_BOOLEAN        = counter++,
    NOT_DECIMAL        = counter++,
    NOT_FLOAT          = counter++,
    NOT_DOUBLE         = counter++,
    INVALID_ENUM_VALUE = counter++,
    OUT_OF_BOUNDS      = counter++,
    NOT_ENUM_VALUE      = counter++,
    FRACTION_GREATER_TOTALDIGITS   = counter++,
    FRACTION_EXCEEDED      = counter++,
    TOTALDIGITS_EXCEEDED   = counter++,
    ILLEGAL_FACET_VALUE    = counter++,
    ILLEGAL_ANYURI_FACET   = counter++,
    ILLEGAL_BOOLEAN_FACET  = counter++,
    ILLEGAL_BASE64_FACET   = counter++,
    ILLEGAL_DATETIME_FACET = counter++,
    ILLEGAL_DECIMAL_FACET  = counter++,
    ILLEGAL_DOUBLE_FACET   = counter++,
    ILLEGAL_FLOAT_FACET    = counter++,
    ILLEGAL_HEXBINARY_FACET  = counter++,
    ILLEGAL_NOTATION_FACET   = counter++,
    ILLEGAL_QNAME_FACET      = counter++,
    ILLEGAL_STRING_FACET     = counter++,
    ILLEGAL_LIST_FACET       = counter++,
    ILLEGAL_UNION_FACET      = counter++,
    ILLEGAL_ANYSIMPLETYPE_FACET  = counter++,

    MSG_MAX_CODE = counter;

    public static final int
    MSG_NONE = 0;

    public static final String[] fgMessageKeys = {
        "BadMajorCode",
        "FormatFailed",
        "NotBoolean",
        "NotDecimal",
        "NotFloat",
        "NotDouble",
        "InvalidEnumValue",
        "OutOfBounds",
        "NotAnEnumValue",
        "FractionDigitsLargerThanTotalDigits",
        "FractionDigitsExceeded",
        "TotalDigitsExceeded",
        "IllegalFacetValue",
        "IllegalAnyURIFacet",
        "IllegalBooleanFacet",
        "IllegalBase64Facet",
        "IllegalDateTimeFacet",
        "IllegalDecimalFacet",
        "IllegalDoubleFacet",
        "IllegalFloatFacet",
        "IllegalHexBinaryFacet",
        "IllegalNotationFacet",
        "IllegalQNameFacet",
        "IllegalStringFacet",
        "IllegalListFacet",
        "IllegalUnionFacet",
        "IllegalAnySimpleTypeFacet"
    };

}
