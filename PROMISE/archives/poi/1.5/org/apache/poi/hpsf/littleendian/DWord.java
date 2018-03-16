package org.apache.poi.hpsf.littleendian;

/**
 * <p>Represents a double word (4 bytes).</p>
 *
 * @author Rainer Klute (klute@rainer-klute.de)
 * @version $Id: DWord.java 352095 2002-02-14 04:00:59Z mjohnson $
 * @since 2002-02-09
 */
public class DWord extends LittleEndian
{

    /**
     * <p>Creates a {@link DWord} and reads its value from a byte
     * array.</p>
     *
     * @param src The byte array to read from.
     *
     * @param offset The offset of the first byte to read.
     */
    public DWord(final byte[] src, final int offset)
    {
        super(src, offset);
    }



    public final static int LENGTH = 4;

    public int length()
    {
        return LENGTH;
    }



    /**
     * <p>Return the integral value of this {@link DWord}.</p>
     *
     * <p><strong>FIXME:</strong> Introduce a superclass for the
     * numeric types and make this a method of the superclass!</p>
     */
    public int intValue()
    {
        int value = 0;
        final int length = length();
        for (int i = 0; i < length; i++)
        {
            final int b = 0xFF & bytes[i];
            value = value << 8 | b;
        }
        return value;
    }

}
