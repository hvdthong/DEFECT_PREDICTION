package org.apache.poi.hpsf;

import java.util.*;
import org.apache.poi.hpsf.littleendian.*;

/**
 * <p>A property in a {@link Section} of a {@link PropertySet}.</p>
 *
 * <p>The property's <strong>ID</strong> gives the property a meaning
 * in the context of its {@link Section}. Each {@link Section} spans
 * its own name space of property IDs.</p>
 *
 * <p>The property's <strong>type</strong> determines how its
 * <strong>value</strong> is interpreted. For example, if the type is
 * {@link Variant#VT_LPSTR} (byte string), the value consists of a
 * {@link DWord} telling how many bytes the string contains. The bytes
 * follow immediately, including any null bytes that terminate the
 * string. The type {@link Variant#VT_I4} denotes a four-byte integer
 * value, {@link Variant#VT_FILETIME} some date and time (of a
 * file).</p>
 *
 * <p><strong>FIXME:</strong> Reading of other types than those
 * mentioned above and the dictionary property is not yet
 * implemented.</p>
 *
 * @see Section
 * @see Variant
 *
 * @author Rainer Klute (klute@rainer-klute.de)
 * @version $Id: Property.java 352095 2002-02-14 04:00:59Z mjohnson $
 * @since 2002-02-09
 */
public class Property
{

    private int id;

    /**
     * <p>Returns the property's ID.</p>
     */
    public int getID()
    {
        return id;
    }



    private int type;

    /**
     * <p>Returns the property's type.</p>
     */
    public int getType()
    {
        return type;
    }



    private Object value;

    /**
     * <p>Returns the property value's.</p>
     */
    public Object getValue()
    {
        return value;
    }



    /**
     * <p>Creates a {@link Property} instance by reading its bytes
     * from the property set stream.</p>
     *
     * @param id The property's ID.
     *
     * @param src The bytes the property set stream consists of.
     *
     * @param offset The property's type/value pair's offset in the
     * section.
     *
     * @param length The property's type/value pair's length in bytes.
     * list.
     */
    public Property(final int id, final byte[] src, final int offset,
                    final int length)
    {
        this.id = id;

        /* ID 0 is a special case since it specifies a dictionary of
         * property IDs and property names. */
        if (id == 0)
        {
            value = readDictionary(src, offset, length);
            return;
        }

        /* FIXME: Support this! */

        int o = offset;
        type = new DWord(src, o).intValue();
        o += DWord.LENGTH;

        /* FIXME: Support reading more types! */
        switch (type)
        {
            case Variant.VT_I4:
            {
                /* Read a word. In Java it is represented as an
                   Integer object. */
                value = new Integer(new DWord(src, o).intValue());
                break;
            }
            case Variant.VT_FILETIME:
            {
                /* Read a FILETIME object. In Java it is represented
                   as a Date. */
                final int low = new DWord(src, o).intValue();
                o += DWord.LENGTH;
                final int high = new DWord(src, o).intValue();
                value = Util.filetimeToDate(high, low);
                break;
            }
            case Variant.VT_LPSTR:
            {
                /* Read a byte string. In Java it is represented as a
                   String. The null bytes at the end of the byte
                   strings must be stripped. */
                final int first = o + DWord.LENGTH;
                int last = first + new DWord(src, o).intValue() - 1;
                o += DWord.LENGTH;
                while (src[last] == 0 && first <= last)
                    last--;
                value = new String(src, first, last - first + 1);
                break;
            }
            default:
            {
                final byte[] v = new byte[length];
                for (int i = 0; i < length; i++)
                    v[i] = src[offset + i];
                value = v;
                break;
            }
        }
    }



    /**
     * <p>Reads a dictionary.</p>
     *
     * @param src The byte array containing the bytes making out the
     * dictionary.
     *
     * @param offset At this offset within <var>src</var> the
     * dictionary starts.
     *
     * @param length The dictionary contains at most this many bytes.
     */
    protected Map readDictionary(final byte[] src, final int offset,
                                 final int length)
    {
        /* FIXME: Check the length! */
        int o = offset;

        /* Read the number of dictionary entries. */
        final int nrEntries = new DWord(src, o).intValue();
        o += DWord.LENGTH;

        final Map m = new HashMap(nrEntries, (float) 1.0);
        for (int i = 0; i < nrEntries; i++)
        {
            /* The key */
            final Integer id = new Integer(new DWord(src, o).intValue());
            o += DWord.LENGTH;

            /* The value (a string) */
            final int sLength = new DWord(src, o).intValue();
            o += DWord.LENGTH;
            /* Strip trailing 0x00 bytes. */
            int l = sLength;
            while (src[o + l - 1] == 0x00)
                l--;
            final String s = new String(src, o, l);
            o += sLength;
            m.put(id, s);
        }
        return m;
    }



    /**
     * <p>Reads a code page.</p>
     *
     * @param src The byte array containing the bytes making out the
     * code page.
     *
     * @param offset At this offset within <var>src</var> the code
     * page starts.
     */
    protected int readCodePage(final byte[] src, final int offset)
    {
        throw new UnsupportedOperationException("FIXME");
    }

}
