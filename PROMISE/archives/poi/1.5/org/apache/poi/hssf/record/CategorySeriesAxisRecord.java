package org.apache.poi.hssf.record;



import org.apache.poi.util.BitField;
import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.StringUtil;
import org.apache.poi.util.HexDump;

/**
 * This record refers to a category or series axis and is used to specify label/tickmark frequency.
 * NOTE: This source is automatically generated please do not modify this file.  Either subclass or
 *       remove the record in src/records/definitions.

 * @author Glen Stampoultzis (glens at apache.org)
 */
public class CategorySeriesAxisRecord
    extends Record
{
    public final static short      sid                             = 0x1020;
    private  short      field_1_crossingPoint;
    private  short      field_2_labelFrequency;
    private  short      field_3_tickMarkFrequency;
    private  short      field_4_options;
    private BitField   valueAxisCrossing                          = new BitField(0x1);
    private BitField   crossesFarRight                            = new BitField(0x2);
    private BitField   reversed                                   = new BitField(0x4);


    public CategorySeriesAxisRecord()
    {

    }

    /**
     * Constructs a CategorySeriesAxis record and sets its fields appropriately.
     *
     * @param id    id must be 0x1020 or an exception
     *              will be throw upon validation
     * @param size  size the size of the data area of the record
     * @param data  data of the record (should not contain sid/len)
     */

    public CategorySeriesAxisRecord(short id, short size, byte [] data)
    {
        super(id, size, data);
    }

    /**
     * Constructs a CategorySeriesAxis record and sets its fields appropriately.
     *
     * @param id    id must be 0x1020 or an exception
     *              will be throw upon validation
     * @param size  size the size of the data area of the record
     * @param data  data of the record (should not contain sid/len)
     * @param offset of the record's data
     */

    public CategorySeriesAxisRecord(short id, short size, byte [] data, int offset)
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
            throw new RecordFormatException("Not a CategorySeriesAxis record");
        }
    }

    protected void fillFields(byte [] data, short size, int offset)
    {
        field_1_crossingPoint           = LittleEndian.getShort(data, 0x0 + offset);
        field_2_labelFrequency          = LittleEndian.getShort(data, 0x2 + offset);
        field_3_tickMarkFrequency       = LittleEndian.getShort(data, 0x4 + offset);
        field_4_options                 = LittleEndian.getShort(data, 0x6 + offset);

    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("[CategorySeriesAxis]\n");

        buffer.append("    .crossingPoint        = ")
            .append("0x")
            .append(HexDump.toHex((short)getCrossingPoint()))
            .append(" (").append(getCrossingPoint()).append(" )\n");

        buffer.append("    .labelFrequency       = ")
            .append("0x")
            .append(HexDump.toHex((short)getLabelFrequency()))
            .append(" (").append(getLabelFrequency()).append(" )\n");

        buffer.append("    .tickMarkFrequency    = ")
            .append("0x")
            .append(HexDump.toHex((short)getTickMarkFrequency()))
            .append(" (").append(getTickMarkFrequency()).append(" )\n");

        buffer.append("    .options              = ")
            .append("0x")
            .append(HexDump.toHex((short)getOptions()))
            .append(" (").append(getOptions()).append(" )\n");
        buffer.append("         .valueAxisCrossing        = ").append(isValueAxisCrossing   ()).append('\n');
        buffer.append("         .crossesFarRight          = ").append(isCrossesFarRight     ()).append('\n');
        buffer.append("         .reversed                 = ").append(isReversed            ()).append('\n');

        buffer.append("[/CategorySeriesAxis]\n");
        return buffer.toString();
    }

    public int serialize(int offset, byte[] data)
    {
        LittleEndian.putShort(data, 0 + offset, sid);
        LittleEndian.putShort(data, 2 + offset, (short)(getRecordSize() - 4));

        LittleEndian.putShort(data, 4 + offset, field_1_crossingPoint);
        LittleEndian.putShort(data, 6 + offset, field_2_labelFrequency);
        LittleEndian.putShort(data, 8 + offset, field_3_tickMarkFrequency);
        LittleEndian.putShort(data, 10 + offset, field_4_options);

        return getRecordSize();
    }

    /**
     * Size of record (exluding 4 byte header)
     */
    public int getRecordSize()
    {
        return 4 + 2 + 2 + 2 + 2;
    }

    public short getSid()
    {
        return this.sid;
    }


    /**
     * Get the crossing point field for the CategorySeriesAxis record.
     */
    public short getCrossingPoint()
    {
        return field_1_crossingPoint;
    }

    /**
     * Set the crossing point field for the CategorySeriesAxis record.
     */
    public void setCrossingPoint(short field_1_crossingPoint)
    {
        this.field_1_crossingPoint = field_1_crossingPoint;
    }

    /**
     * Get the label frequency field for the CategorySeriesAxis record.
     */
    public short getLabelFrequency()
    {
        return field_2_labelFrequency;
    }

    /**
     * Set the label frequency field for the CategorySeriesAxis record.
     */
    public void setLabelFrequency(short field_2_labelFrequency)
    {
        this.field_2_labelFrequency = field_2_labelFrequency;
    }

    /**
     * Get the tick mark frequency field for the CategorySeriesAxis record.
     */
    public short getTickMarkFrequency()
    {
        return field_3_tickMarkFrequency;
    }

    /**
     * Set the tick mark frequency field for the CategorySeriesAxis record.
     */
    public void setTickMarkFrequency(short field_3_tickMarkFrequency)
    {
        this.field_3_tickMarkFrequency = field_3_tickMarkFrequency;
    }

    /**
     * Get the options field for the CategorySeriesAxis record.
     */
    public short getOptions()
    {
        return field_4_options;
    }

    /**
     * Set the options field for the CategorySeriesAxis record.
     */
    public void setOptions(short field_4_options)
    {
        this.field_4_options = field_4_options;
    }

    /**
     * Sets the value axis crossing field value.
     * set true to indicate axis crosses between categories and false to cross axis midway
     */
    public void setValueAxisCrossing(boolean value)
    {
        field_4_options = valueAxisCrossing.setShortBoolean(field_4_options, value);
    }

    /**
     * set true to indicate axis crosses between categories and false to cross axis midway
     * @return  the value axis crossing field value.
     */
    public boolean isValueAxisCrossing()
    {
        return valueAxisCrossing.isSet(field_4_options);
    }

    /**
     * Sets the crosses far right field value.
     * axis crosses at the far right
     */
    public void setCrossesFarRight(boolean value)
    {
        field_4_options = crossesFarRight.setShortBoolean(field_4_options, value);
    }

    /**
     * axis crosses at the far right
     * @return  the crosses far right field value.
     */
    public boolean isCrossesFarRight()
    {
        return crossesFarRight.isSet(field_4_options);
    }

    /**
     * Sets the reversed field value.
     * categories are displayed in reverse order
     */
    public void setReversed(boolean value)
    {
        field_4_options = reversed.setShortBoolean(field_4_options, value);
    }

    /**
     * categories are displayed in reverse order
     * @return  the reversed field value.
     */
    public boolean isReversed()
    {
        return reversed.isSet(field_4_options);
    }






