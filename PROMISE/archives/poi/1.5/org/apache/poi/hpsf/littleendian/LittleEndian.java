package org.apache.poi.hpsf.littleendian;

/**
 * <p>A data item in the little-endian format. Little-endian means
 * that lower bytes come before higher bytes.</p>
 *
 * @author Rainer Klute (klute@rainer-klute.de)
 * @version $Id: LittleEndian.java 352095 2002-02-14 04:00:59Z mjohnson $
 * @since 2002-02-09
 */
public abstract class LittleEndian
{

    /* This class could be optimized by not copying the bytes, but
     * instead maintaining just references to the original byte
     * arrays. However, before implementing this it should be
     * investigated whether it is worth the while. */


    /**
     * <p>The bytes making out the little-endian field. They are in
     * correct order, i.e. high-endian.</p>
     */
    protected byte[] bytes;



    /**
     * <p>Creates a {@link LittleEndian} and reads its value from a
     * byte array.</p>
     *
     * @param src The byte array to read from.
     *
     * @param offset The offset of the first byte to read.
     */
    public LittleEndian(final byte[] src, final int offset)
    {
        read(src, offset);
    }



    /**
     * <p>Returns the bytes making out the little-endian field in
     * big-endian order.
     </p> */
    public byte[] getBytes()
    {
        return bytes;
    }



    /**
     * <p>Reads the little-endian field from a byte array.</p>
     *
     * @param src The byte array to read from
     *
     * @param offset The offset within the <var>src</var> byte array
     */
    public byte[] read(final byte[] src, final int offset)
    {
        final int length = length();
        bytes = new byte[length];
        for (int i = 0; i < length; i++)
            bytes[i] = src[offset + length - 1 - i];
        return bytes;
    }


    /**
     * <p>Returns the number of bytes of this little-endian field.</p>
     */
    public abstract int length();

}
