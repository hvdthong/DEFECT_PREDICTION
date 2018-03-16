package org.apache.poi.hpsf.littleendian;

/**
 * <p>Represents a byte.</p>
 *
 * @author Rainer Klute (klute@rainer-klute.de)
 * @version $Id: Byte.java 352095 2002-02-14 04:00:59Z mjohnson $
 * @since 2002-02-09
 */
public class Byte extends LittleEndian
{

    /**
     * <p>Creates a {@link Byte} and reads its value from a byte
     * array.</p>
     *
     * @param src The byte array to read from.
     *
     * @param offset The offset of the byte to read.
     */
    public Byte(final byte[] src, final int offset)
    {
        super(src, offset);
    }



    public final static int LENGTH = 1;

    public int length()
    {
        return LENGTH;
    }

}
