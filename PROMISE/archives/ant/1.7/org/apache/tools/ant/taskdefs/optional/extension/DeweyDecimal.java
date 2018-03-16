package org.apache.tools.ant.taskdefs.optional.extension;


/**
 * Utility class to contain version numbers in "Dewey Decimal"
 * syntax.  Numbers in the "Dewey Decimal" syntax consist of positive
 * decimal integers separated by periods ".".  For example, "2.0" or
 * "1.2.3.4.5.6.7".  This allows an extensible number to be used to
 * represent major, minor, micro, etc versions.  The version number
 * must begin with a number.
 *
 * Original Implementation moved to org.apache.tools.ant.util.DeweyDecimal
 */
public final class DeweyDecimal extends org.apache.tools.ant.util.DeweyDecimal {

    /**
     * Construct a DeweyDecimal from an array of integer components.
     *
     * @param components an array of integer components.
     */
    public DeweyDecimal(final int[] components) {
        super(components);
    }

    /**
     * Construct a DeweyDecimal from string in DeweyDecimal format.
     *
     * @param string the string in dewey decimal format
     * @exception NumberFormatException if string is malformed
     */
    public DeweyDecimal(final String string)
        throws NumberFormatException {
        super(string);
    }
}
