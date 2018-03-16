package org.apache.poi.hpsf.littleendian;

/**
 * <p>Represents a word (2 bytes).</p>
 *
 * @author Rainer Klute (klute@rainer-klute.de)
 * @version $Id: Word.java 352095 2002-02-14 04:00:59Z mjohnson $
 * @since 2002-02-09
 */
public class Word extends LittleEndian
{

    /**
     * <p>Creates a {@link Word} and reads its value from a byte
     * array.</p>
     *
     * @param src The byte array to read from.
     *
     * @param offset The offset of the first byte to read.
     */
    public Word(final byte[] src, final int offset)
    {
        super(src, offset);
    }


    public final static int LENGTH = 2;

    public int length()
    {
        return LENGTH;
    }

}
