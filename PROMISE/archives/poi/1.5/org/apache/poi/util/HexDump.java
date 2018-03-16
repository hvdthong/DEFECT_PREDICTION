package org.apache.poi.util;

import java.io.*;

/**
 * dump data in hexadecimal format; derived from a HexDump utility I
 * wrote in June 2001.
 *
 * @author Marc Johnson
 * @author Glen Stampoultzis  (glens at apache.org)
 */

public class HexDump
{

    private HexDump()
    {
    }

    /**
     * dump an array of bytes to an OutputStream
     *
     * @param data the byte array to be dumped
     * @param offset its offset, whatever that might mean
     * @param stream the OutputStream to which the data is to be
     *               written
     * @param index initial index into the byte array
     *
     * @exception IOException is thrown if anything goes wrong writing
     *            the data to stream
     * @exception ArrayIndexOutOfBoundsException if the index is
     *            outside the data array's bounds
     * @exception IllegalArgumentException if the output stream is
     *            null
     */

    public synchronized static void dump(final byte [] data, final long offset,
                            final OutputStream stream, final int index)
        throws IOException, ArrayIndexOutOfBoundsException,
                IllegalArgumentException
    {
        if ((index < 0) || (index >= data.length))
        {
            throw new ArrayIndexOutOfBoundsException(
                "illegal index: " + index + " into array of length "
                + data.length);
        }
        if (stream == null)
        {
            throw new IllegalArgumentException("cannot write to nullstream");
        }
        long         display_offset = offset + index;
        StringBuffer buffer         = new StringBuffer(74);

        for (int j = index; j < data.length; j += 16)
        {
            int chars_read = data.length - j;

            if (chars_read > 16)
            {
                chars_read = 16;
            }
            buffer.append(dump(display_offset)).append(' ');
            for (int k = 0; k < 16; k++)
            {
                if (k < chars_read)
                {
                    buffer.append(dump(data[ k + j ]));
                }
                else
                {
                    buffer.append("  ");
                }
                buffer.append(' ');
            }
            for (int k = 0; k < chars_read; k++)
            {
                if ((data[ k + j ] >= ' ') && (data[ k + j ] < 127))
                {
                    buffer.append(( char ) data[ k + j ]);
                }
                else
                {
                    buffer.append('.');
                }
            }
            buffer.append(EOL);
            stream.write(buffer.toString().getBytes());
            stream.flush();
            buffer.setLength(0);
            display_offset += chars_read;
        }
    }

    public static final String        EOL         =
        System.getProperty("line.separator");
    private static final StringBuffer _lbuffer    = new StringBuffer(8);
    private static final StringBuffer _cbuffer    = new StringBuffer(2);
    private static final char         _hexcodes[] =
    {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
        'E', 'F'
    };
    private static final int          _shifts[]   =
    {
        28, 24, 20, 16, 12, 8, 4, 0
    };

    private static StringBuffer dump(final long value)
    {
        _lbuffer.setLength(0);
        for (int j = 0; j < 8; j++)
        {
            _lbuffer
                .append(_hexcodes[ (( int ) (value >> _shifts[ j ])) & 15 ]);
        }
        return _lbuffer;
    }

    private static StringBuffer dump(final byte value)
    {
        _cbuffer.setLength(0);
        for (int j = 0; j < 2; j++)
        {
            _cbuffer.append(_hexcodes[ (value >> _shifts[ j + 6 ]) & 15 ]);
        }
        return _cbuffer;
    }

    /**
     * Converts the parameter to a hex value.
     *
     * @param value     The value to convert
     * @return          A String representing the array of bytes
     */
    public static String toHex(final byte[] value)
    {
        StringBuffer retVal = new StringBuffer();
        retVal.append('[');
        for(int x = 0; x < value.length; x++)
        {
            retVal.append(toHex(value[x]));
            retVal.append(", ");
        }
        retVal.append(']');
        return retVal.toString();
    }
    /**
     * Converts the parameter to a hex value.
     *
     * @param value     The value to convert
     * @return          The result right padded with 0
     */
    public static String toHex(final short value)
    {
        return toHex(value, 4);
    }

    /**
     * Converts the parameter to a hex value.
     *
     * @param value     The value to convert
     * @return          The result right padded with 0
     */
    public static String toHex(final byte value)
    {
        return toHex(value, 2);
    }

    /**
     * Converts the parameter to a hex value.
     *
     * @param value     The value to convert
     * @return          The result right padded with 0
     */
    public static String toHex(final int value)
    {
        return toHex(value, 8);
    }


    private static String toHex(final long value, final int digits)
    {
        StringBuffer result = new StringBuffer(digits);
        for (int j = 0; j < digits; j++)
        {
            result.append( _hexcodes[ (int) ((value >> _shifts[ j + (8 - digits) ]) & 15)]);
        }
        return result.toString();
    }
}
