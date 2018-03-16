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
        { "BadMajorCode", "The majorCode parameter to createMessage was out of bounds." },
        { "FormatFailed", "An internal error occurred while formatting the following message:\n  " },
        { "NotBoolean", "{0} is not a boolean." },
        { "NotDecimal", "{0} is not a decimal." },
        { "FacetsInconsistent", "Facets are inconsistent with base type." },
        { "IllegalFacetValue", "Illegal value {0} for facet {1}." },
        { "IllegalDecimalFacet", "Illegal Facet for decimal type." },
        { "UnknownFacet", "Unknown Facet: {0}." },
        { "InvalidEnumValue", "Invalid value for Enum constant: {0}." },
        { "OutOfBounds", "{0} is out of bounds:[ {1} {3} X  {4} {2} ]." },
        { "NotAnEnumValue", "{0} is not one of the specified enum values." },
        { "NotInteger", "{0} is not an integer." },
        { "IllegalIntegerFacet", "Illegal Facet for Integer type." },
        { "NotReal", "{0} is not a double." },
        { "IllegalRealFacet", "Illegal Facet for Real type." },
        { "ScaleLargerThanPrecision", "Scale Facet must be less than or equal to Precision Facet" },
        { "PrecisionExceeded", "{0} has exceeded the precision Facet {1}"},
        { "ScaleExceeded", "{0} has execeed the scale Facet {1}"},
        { "NotFloat", "{0} is not a float." },
    };

    /** Returns the list resource bundle contents. */
    public Object[][] getContents() {
        return CONTENTS;
    }
}
