package org.apache.poi.hssf.record;



import org.apache.poi.util.BitField;
import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.StringUtil;
import org.apache.poi.util.HexDump;

/**
 * The frame record indicates whether there is a border around the displayed text of a chart.
 * NOTE: This source is automatically generated please do not modify this file.  Either subclass or
 *       remove the record in src/records/definitions.

 * @author Glen Stampoultzis (glens at apache.org)
 */
public class FrameRecord
    extends Record
{
    public final static short      sid                             = 0x1032;
    private  short      field_1_borderType;
    public final static short       BORDER_TYPE_REGULAR            = 0;
    public final static short       BORDER_TYPE_SHADOW             = 1;
    private  short      field_2_options;
    private BitField   autoSize                                   = new BitField(0x1);
    private BitField   autoPosition                               = new BitField(0x2);


    public FrameRecord()
    {

    }

    /**
     * Constructs a Frame record and sets its fields appropriately.
     *
     * @param id    id must be 0x1032 or an exception
     *              will be throw upon validation
     * @param size  size the size of the data area of the record
     * @param data  data of the record (should not contain sid/len)
     */

    public FrameRecord(short id, short size, byte [] data)
    {
        super(id, size, data);
    }

    /**
     * Constructs a Frame record and sets its fields appropriately.
     *
     * @param id    id must be 0x1032 or an exception
     *              will be throw upon validation
     * @param size  size the size of the data area of the record
     * @param data  data of the record (should not contain sid/len)
     * @param offset of the record's data
     */

    public FrameRecord(short id, short size, byte [] data, int offset)
    {
        super(id, size, data, offset);
    }

    /**
     * Checks the sid matches the expected side for this record
     *
     * @param id   the expected sid.
     */
    protected void validateSid(short id)
    {
        if (id != sid)
        {
            throw new RecordFormatException("Not a Frame record");
        }
    }

    protected void fillFields(byte [] data, short size, int offset)
    {
        field_1_borderType              = LittleEndian.getShort(data, 0x0 + offset);
        field_2_options                 = LittleEndian.getShort(data, 0x2 + offset);

    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("[Frame]\n");

        buffer.append("    .borderType           = ")
            .append("0x")
            .append(HexDump.toHex((short)getBorderType()))
            .append(" (").append(getBorderType()).append(" )\n");

        buffer.append("    .options              = ")
            .append("0x")
            .append(HexDump.toHex((short)getOptions()))
            .append(" (").append(getOptions()).append(" )\n");
        buffer.append("         .autoSize                 = ").append(isAutoSize            ()).append('\n');
        buffer.append("         .autoPosition             = ").append(isAutoPosition        ()).append('\n');

        buffer.append("[/Frame]\n");
        return buffer.toString();
    }

    public int serialize(int offset, byte[] data)
    {
        LittleEndian.putShort(data, 0 + offset, sid);
        LittleEndian.putShort(data, 2 + offset, (short)(getRecordSize() - 4));

        LittleEndian.putShort(data, 4 + offset, field_1_borderType);
        LittleEndian.putShort(data, 6 + offset, field_2_options);

        return getRecordSize();
    }

    /**
     * Size of record (exluding 4 byte header)
     */
    public int getRecordSize()
    {
        return 4 + 2 + 2;
    }

    public short getSid()
    {
        return this.sid;
    }


    /**
     * Get the border type field for the Frame record.
     *
     * @return  One of 
     *        BORDER_TYPE_REGULAR
     *        BORDER_TYPE_SHADOW
     */
    public short getBorderType()
    {
        return field_1_borderType;
    }

    /**
     * Set the border type field for the Frame record.
     *
     * @param field_1_borderType
     *        One of 
     *        BORDER_TYPE_REGULAR
     *        BORDER_TYPE_SHADOW
     */
    public void setBorderType(short field_1_borderType)
    {
        this.field_1_borderType = field_1_borderType;
    }

    /**
     * Get the options field for the Frame record.
     */
    public short getOptions()
    {
        return field_2_options;
    }

    /**
     * Set the options field for the Frame record.
     */
    public void setOptions(short field_2_options)
    {
        this.field_2_options = field_2_options;
    }

    /**
     * Sets the auto size field value.
     * excel calculates the size automatically if true
     */
    public void setAutoSize(boolean value)
    {
        field_2_options = autoSize.setShortBoolean(field_2_options, value);
    }

    /**
     * excel calculates the size automatically if true
     * @return  the auto size field value.
     */
    public boolean isAutoSize()
    {
        return autoSize.isSet(field_2_options);
    }

    /**
     * Sets the auto position field value.
     * excel calculates the position automatically
     */
    public void setAutoPosition(boolean value)
    {
        field_2_options = autoPosition.setShortBoolean(field_2_options, value);
    }

    /**
     * excel calculates the position automatically
     * @return  the auto position field value.
     */
    public boolean isAutoPosition()
    {
        return autoPosition.isSet(field_2_options);
    }






