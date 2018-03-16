package org.apache.xerces.validators.schema;

import org.apache.xerces.utils.XMLMessageProvider;
import java.util.ResourceBundle;
import java.util.ListResourceBundle;
import java.util.Locale;

/**
 * SchemaMessageProvider implements an XMLMessageProvider that
 * provides localizable error messages for the W3C XML Schema Language
 *
 */
public class SchemaMessageProvider implements XMLMessageProvider {
    /**
     * The domain of messages concerning the XML Schema: Structures specification.
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
                fResourceBundle = ListResourceBundle.getBundle("org.apache.xerces.msg.SchemaMessages", locale);
            if (fResourceBundle == null)
                fResourceBundle = ListResourceBundle.getBundle("org.apache.xerces.msg.SchemaMessages");
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
        NoValidatorFor = 2,
        IncorrectDatatype = 3,
        AttMissingType = 4,
        NotADatatype = 5,
        TextOnlyContentWithType = 6,
        FeatureUnsupported = 7,
        NestedOnlyInElemOnly = 8,
        EltRefOnlyInMixedElemOnly = 9,
        OnlyInEltContent = 10,
        OrderIsAll = 11,
        DatatypeWithType = 12,
        DatatypeQualUnsupported = 13,
        GroupContentRestricted = 14,
        UnknownBaseDatatype = 15,
        BadAttWithRef = 16,
        NoContentForRef = 17,
        IncorrectDefaultType = 18,
        IllegalAttContent = 19,
        ValueNotInteger = 20,
        DatatypeError = 21,
		TypeAlreadySet = 22,
		GenericError = 23,
		UnclassifiedError = 24,
        ContentError = 25,
        AnnotationError = 26,
        ListUnionRestrictionError = 27,
        ProhibitedAttributePresent = 28,
        UniqueNotEnoughValues = 29,
        KeyNotEnoughValues = 30,
        KeyRefNotEnoughValues = 31,
        DuplicateField = 32,
        DuplicateUnique = 33,
        DuplicateKey = 34,
        KeyNotFound = 35,
        UnknownField = 36,
        MSG_MAX_CODE = 37;
    public static final int
        MSG_NONE = 0;

    public static final String[] fgMessageKeys = {
        "UniqueNotEnoughValues",
        "KeyNotEnoughValues",
        "KeyRefNotEnoughValues",
        "DuplicateField",
        "DuplicateUnique",
        "DuplicateKey",
        "KeyNotFound",
        "UnknownField",
    };
}
