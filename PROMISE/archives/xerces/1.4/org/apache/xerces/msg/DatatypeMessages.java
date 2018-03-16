package org.apache.xerces.msg;

import java.util.ListResourceBundle;

/**
 * This file contains error and warning messages for the schema datatype validator
 * The messages are arranged in key and value tuples in a ListResourceBundle.
 *
 *
 * @version
 */
public class DatatypeMessages extends ListResourceBundle {
    /** The list resource bundle contents. */
    public static final Object CONTENTS[][] = {

        { "BadMajorCode", "The majorCode parameter to createMessage was out of bounds" },
        { "FormatFailed", "An internal error occurred while formatting the following message:\n  " },
        { "NotBoolean", "{0} is not a boolean" },
        { "NotDecimal", "{0} is not a decimal" },
        { "NotFloat", "{0} is not a float" },
        { "NotDouble", "{0} is not a double" },
        { "InvalidEnumValue", "Invalid value for Enum constant: {0}" },
        { "OutOfBounds", "{0} is out of bounds:[ {1} {3} X  {4} {2} ]" },
        { "NotAnEnumValue", "{0} is not one of the specified enum values" },
        { "FractionDigitsLargerThanTotalDigits", "FractionDigits Facet must be less than or equal to TotalDigits Facet" },
        { "TotalDigitsExceeded", "{0} has exceeded the totalDigits Facet {1}"},
        { "FractionDigitsExceeded", "{0} has execeed the fractionDigits Facet {1}"},
        { "IllegalFacetValue", "Illegal value {0} for facet {1}" },
        { "IllegalAnyURIFacet", "Illegal facet {0} for anyURI type" },
        { "IllegalBooleanFacet", "Illegal facet {0} for boolean type" },
        { "IllegalBase64Facet", "Illegal facet {0} for base64Binary type" },
        { "IllegalDateTimeFacet", "Illegal facet {0} for date/time types" },
        { "IllegalDecimalFacet", "Illegal facet {0} for decimal type" },
        { "IllegalDoubleFacet", "Illegal facet {0} for double type" },
        { "IllegalFloatFacet", "Illegal facet {0} for float type" },
        { "IllegalHexBinaryFacet", "Illegal facet {0} for hexBinary type" },
        { "IllegalNotationFacet", "Illegal facet {0} for NOTATION type" },
        { "IllegalQNameFacet", "Illegal facet {0} for  QName type" },
        { "IllegalStringFacet", "Illegal facet {0} for string type" },
        { "IllegalListFacet", "Illegal facet {0} for list type" },
        { "IllegalUnionFacet", "Illegal facet {0} for union type" },
        { "IllegalAnySimpleTypeFacet", "Cannot specify any facet for anySimpleType" },
    };

    /** Returns the list resource bundle contents. */
    public Object[][] getContents() {
        return CONTENTS;
    }
}
