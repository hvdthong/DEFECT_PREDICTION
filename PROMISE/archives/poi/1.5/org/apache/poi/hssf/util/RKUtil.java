package org.apache.poi.hssf.util;

/**
 * Utility class for helping convert RK numbers.
 *
 * @author Andrew C. Oliver (acoliver at apache dot org)
 * @author Glen Stampoultzis (glens at apache.org)
 * @author Rolf-Jürgen Moll
 *
 * @see org.apache.poi.hssf.record.MulRKRecord
 * @see org.apache.poi.hssf.record.RKRecord
 */
public class RKUtil
{
    private RKUtil()
    {
    }

    /**
     * Do the dirty work of decoding; made a private static method to
     * facilitate testing the algorithm
     */

    public static double decodeNumber(int number)
    {
        long raw_number = number;

        raw_number = raw_number >> 2;
        double rvalue = 0;

        if ((number & 0x02) == 0x02)
        {
            rvalue = ( double ) (raw_number);
        }
        else
        {

            rvalue = Double.longBitsToDouble(raw_number << 34);
        }
        if ((number & 0x01) == 0x01)
        {

            rvalue /= 100;
        }

        return rvalue;
    }

}
