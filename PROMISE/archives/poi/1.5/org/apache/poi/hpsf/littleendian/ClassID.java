package org.apache.poi.hpsf.littleendian;

import java.io.*;

/**
 * <p>Represents a class ID (16 bytes). Unlike other little-endian
 * type the {@link ClassID} is not just 16 bytes stored in the wrong
 * order. Instead, it is a double word (4 bytes) followed by two words
 * (2 bytes each) followed by 8 bytes.</p>
 *
 * @see LittleEndian
 *
 * @author Rainer Klute (klute@rainer-klute.de)
 * @version $Id: ClassID.java 352095 2002-02-14 04:00:59Z mjohnson $
 * @since 2002-02-09
 */
public class ClassID extends LittleEndian
{

    /**
     * <p>Creates a {@link ClassID} and reads its value from a byte
     * array.</p>
     *
     * @param src The byte array to read from.
     *
     * @param offset The offset of the first byte to read.
     */
    public ClassID(final byte[] src, final int offset)
    {
        super(src, offset);
    }



    public final static int LENGTH = 16;

    public int length()
    {
        return LENGTH;
    }



    public byte[] read(byte[] src, int offset)
    {
        LittleEndian[] b = new LittleEndian[11];
        b[0] = new DWord(src, offset);
        b[1] = new Word(src, offset += DWord.LENGTH);
        b[2] = new Word(src, offset += Word.LENGTH);
        b[3] = new Byte(src, offset += Word.LENGTH);
        b[4] = new Byte(src, offset += Byte.LENGTH);
        b[5] = new Byte(src, offset += Byte.LENGTH);
        b[6] = new Byte(src, offset += Byte.LENGTH);
        b[7] = new Byte(src, offset += Byte.LENGTH);
        b[8] = new Byte(src, offset += Byte.LENGTH);
        b[9] = new Byte(src, offset += Byte.LENGTH);
        b[10] = new Byte(src, offset += Byte.LENGTH);
        int capacity = 0;
        for (int i = 0; i < b.length; i++)
            capacity += b[i].getBytes().length;
        bytes = new byte[capacity];
        int pos = 0;
        for (int i = 0; i < b.length; i++)
        {
            byte[] s = b[i].getBytes();
            for (int j = 0; j < s.length; j++)
                bytes[pos++] = s[j];
        }
        return bytes;
    }

}
