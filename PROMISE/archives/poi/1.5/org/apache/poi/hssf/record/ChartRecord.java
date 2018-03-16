package org.apache.poi.hssf.record;



import org.apache.poi.util.BitField;
import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.StringUtil;
import org.apache.poi.util.HexDump;

/**
 * The chart record is used to define the location and size of a chart.
 * NOTE: This source is automatically generated please do not modify this file.  Either subclass or
 *       remove the record in src/records/definitions.

 * @author Glen Stampoultzis (glens at apache.org)
 */
public class ChartRecord
    extends Record
{
    public final static short      sid                             = 0x1002;
    private  int        field_1_x;
    private  int        field_2_y;
    private  int        field_3_width;
    private  int        field_4_height;


    public ChartRecord()
    {

    }

    /**
     * Constructs a Chart record and sets its fields appropriately.
     *
     * @param id    id must be 0x1002 or an exception
     *              will be throw upon validation
     * @param size  size the size of the data area of the record
     * @param data  data of the record (should not contain sid/len)
     */

    public ChartRecord(short id, short size, byte [] data)
    {
        super(id, size, data);
    }

    /**
     * Constructs a Chart record and sets its fields appropriately.
     *
     * @param id    id must be 0x1002 or an exception
     *              will be throw upon validation
     * @param size  size the size of the data area of the record
     * @param data  data of the record (should not contain sid/len)
     * @param offset of the record's data
     */

    public ChartRecord(short id, short size, byte [] data, int offset)
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
            throw new RecordFormatException("Not a Chart record");
        }
    }

    protected void fillFields(byte [] data, short size, int offset)
    {
        field_1_x                       = LittleEndian.getInt(data, 0x0 + offset);
        field_2_y                       = LittleEndian.getInt(data, 0x4 + offset);
        field_3_width                   = LittleEndian.getInt(data, 0x8 + offset);
        field_4_height                  = LittleEndian.getInt(data, 0xc + offset);

    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("[Chart]\n");

        buffer.append("    .x                    = ")
            .append("0x")
            .append(HexDump.toHex((int)getX()))
            .append(" (").append(getX()).append(" )\n");

        buffer.append("    .y                    = ")
            .append("0x")
            .append(HexDump.toHex((int)getY()))
            .append(" (").append(getY()).append(" )\n");

        buffer.append("    .width                = ")
            .append("0x")
            .append(HexDump.toHex((int)getWidth()))
            .append(" (").append(getWidth()).append(" )\n");

        buffer.append("    .height               = ")
            .append("0x")
            .append(HexDump.toHex((int)getHeight()))
            .append(" (").append(getHeight()).append(" )\n");

        buffer.append("[/Chart]\n");
        return buffer.toString();
    }

    public int serialize(int offset, byte[] data)
    {
        LittleEndian.putShort(data, 0 + offset, sid);
        LittleEndian.putShort(data, 2 + offset, (short)(getRecordSize() - 4));

        LittleEndian.putInt(data, 4 + offset, field_1_x);
        LittleEndian.putInt(data, 8 + offset, field_2_y);
        LittleEndian.putInt(data, 12 + offset, field_3_width);
        LittleEndian.putInt(data, 16 + offset, field_4_height);

        return getRecordSize();
    }

    /**
     * Size of record (exluding 4 byte header)
     */
    public int getRecordSize()
    {
        return 4 + 4 + 4 + 4 + 4;
    }

    public short getSid()
    {
        return this.sid;
    }


    /**
     * Get the x field for the Chart record.
     */
    public int getX()
    {
        return field_1_x;
    }

    /**
     * Set the x field for the Chart record.
     */
    public void setX(int field_1_x)
    {
        this.field_1_x = field_1_x;
    }

    /**
     * Get the y field for the Chart record.
     */
    public int getY()
    {
        return field_2_y;
    }

    /**
     * Set the y field for the Chart record.
     */
    public void setY(int field_2_y)
    {
        this.field_2_y = field_2_y;
    }

    /**
     * Get the width field for the Chart record.
     */
    public int getWidth()
    {
        return field_3_width;
    }

    /**
     * Set the width field for the Chart record.
     */
    public void setWidth(int field_3_width)
    {
        this.field_3_width = field_3_width;
    }

    /**
     * Get the height field for the Chart record.
     */
    public int getHeight()
    {
        return field_4_height;
    }

    /**
     * Set the height field for the Chart record.
     */
    public void setHeight(int field_4_height)
    {
        this.field_4_height = field_4_height;
    }






