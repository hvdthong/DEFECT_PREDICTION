package org.apache.poi.hpsf;

import java.util.*;
import org.apache.poi.hpsf.littleendian.*;
import org.apache.poi.hpsf.wellknown.*;

/**
 * <p>Represents a section in a {@link PropertySet}.</p>
 *
 * @author Rainer Klute (klute@rainer-klute.de)
 * @version $Id: Section.java 352095 2002-02-14 04:00:59Z mjohnson $
 * @since 2002-02-09
 */
public class Section
{

    /**
     * <p>Maps property IDs to section-private PID strings. These
     * strings can be found in the property with ID 0.</p>
     */
    protected Map dictionary;



    private ClassID formatID;

    /**
     * <p>Returns the format ID. The format ID is the "type" of the
     * section.</p>
     */
    public ClassID getFormatID()
    {
        return formatID;
    }



    private int offset;

    /**
     * <p>Returns the offset of the section in the stream.</p>
     */
    public int getOffset()
    {
        return offset;
    }



    private int size;

    /**
     * <p>Returns the section's size in bytes.</p>
     */
    public int getSize()
    {
        return size;
    }



    private int propertyCount;

    /**
     * <p>Returns the number of properties in this section.</p>
     */
    public int getPropertyCount()
    {
        return propertyCount;
    }



    private Property[] properties;

    /**
     * <p>Returns this section's properties.</p>
     */
    public Property[] getProperties()
    {
        return properties;
    }



    /**
     * <p>Creates a {@link Section} instance from a byte array.</p>
     *
     * @param src Contains the complete property set stream.
     *
     * @param offset The position in the stream that points to the
     * section's format ID.
     */
    public Section(final byte[] src, int offset)
    {
        /* Read the format ID. */
        formatID = new ClassID(src, offset);
        offset += ClassID.LENGTH;

        /* Read the offset from the stream's start and positions to
         * the section header. */
        this.offset = new DWord(src, offset).intValue();
        offset = this.offset;

        /* Read the section length. */
        size = new DWord(src, offset).intValue();
        offset += DWord.LENGTH;

        /* Read the number of properties. */
        propertyCount = new DWord(src, offset).intValue();
        offset += DWord.LENGTH;

        /* Read the properties. The offset is positioned at the first
         * entry of the property list. */
        properties = new Property[propertyCount];
        for (int i = 0; i < properties.length; i++)
        {
            final int id = new DWord(src, offset).intValue();
            offset += DWord.LENGTH;

            /* Offset from the section. */
            final int sOffset = new DWord(src, offset).intValue();
            offset += DWord.LENGTH;

            /* Calculate the length of the property. */
            int length;
            if (i == properties.length - 1)
                length = src.length - this.offset - sOffset;
            else
                length =
                    new DWord(src, offset + DWord.LENGTH).intValue() - sOffset;

            /* Create it. */
            properties[i] =
                new Property(id, src, this.offset + sOffset, length);
        }

        /* Extract the dictionary (if available). */
        dictionary = (Map) getProperty(0);
    }



    /**
     * <p>Returns the value of the property with the specified ID. If
     * the property is not available, <code>null</code> is returned
     * and a subsequent call to {@link #wasNull} will return
     * <code>true</code>.</p>
     */
    protected Object getProperty(final int id)
    {
        wasNull = false;
        for (int i = 0; i < properties.length; i++)
            if (id == properties[i].getID())
                return properties[i].getValue();
        wasNull = true;
        return null;
    }



    /**
     * <p>Returns the value of the numeric property with the specified
     * ID. If the property is not available, 0 is returned. A
     * subsequent call to {@link #wasNull} will return
     * <code>true</code> to let the caller distinguish that case from
     * a real property value of 0.</p>
     */
    protected int getPropertyIntValue(final int id)
    {
        final Integer i = (Integer) getProperty(id);
        if (i != null)
            return i.intValue();
        else
            return 0;
    }



    private boolean wasNull;

    /**
     * <p>Checks whether the property which the last call to {@link
     * #getPropertyIntValue} or {@link #getProperty} tried to access
     * was available or not. This information might be important for
     * callers of {@link #getPropertyIntValue} since the latter
     * returns 0 if the property does not exist. Using {@link
     * #wasNull} the caller can distiguish this case from a property's
     * real value of 0.</p>
     *
     * @return <code>true</code> if the last call to {@link
     * #getPropertyIntValue} or {@link #getProperty} tried to access a
     * property that was not available, else <code>false</code>.
     */
    public boolean wasNull()
    {
        return wasNull;
    }



    /**
     * <p>Returns the PID string associated with a property ID. The ID
     * is first looked up in the {@link Section}'s private
     * dictionary. If it is not found there, the method calls {@link
     * SectionIDMap#getPIDString}.</p>
     */
    public String getPIDString(final int pid)
    {
        String s = null;
        if (dictionary != null)
            s = (String) dictionary.get(new Integer(pid));
        if (s == null)
            s =  SectionIDMap.getPIDString(getFormatID().getBytes(), pid);
        if (s == null)
            s = SectionIDMap.UNDEFINED;
        return s;
    }

}
