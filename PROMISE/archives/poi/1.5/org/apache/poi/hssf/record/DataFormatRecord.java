package org.apache.poi.hssf.record;



import org.apache.poi.util.BitField;
import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.StringUtil;
import org.apache.poi.util.HexDump;

/**
 * The data format record is used to index into a series.
 * NOTE: This source is automatically generated please do not modify this file.  Either subclass or
 *       remove the record in src/records/definitions.

 * @author Glen Stampoultzis (glens at apache.org)
 */
public class DataFormatRecord
    extends Record
{
    public final static short      sid                             = 0x1006;
    private  short      field_1_pointNumber;
    private  short      field_2_seriesIndex;
    private  short      field_3_seriesNumber;
    private  short      field_4_formatFlags;
    private BitField   useExcel4Colors                            = new BitField(0x1);


    public DataFormatRecord()
    {

    }

    /**
     * Constructs a DataFormat record and sets its fields appropriately.
     *
     * @param id    id must be 0x1006 or an exception
     *              will be throw upon validation
     * @param size  size the size of the data area of the record
     * @param data  data of the record (should not contain sid/len)
     */

    public DataFormatRecord(short id, short size, byte [] data)
    {
        super(id, size, data);
    }

    /**
     * Constructs a DataFormat record and sets its fields appropriately.
     *
     * @param id    id must be 0x1006 or an exception
     *              will be throw upon validation
     * @param size  size the size of the data area of the record
     * @param data  data of the record (should not contain sid/len)
     * @param offset of the record's data
     */

    public DataFormatRecord(short id, short size, byte [] data, int offset)
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
            throw new RecordFormatException("Not a DataFormat record");
        }
    }

    protected void fillFields(byte [] data, short size, int offset)
    {
        field_1_pointNumber             = LittleEndian.getShort(data, 0x0 + offset);
        field_2_seriesIndex             = LittleEndian.getShort(data, 0x2 + offset);
        field_3_seriesNumber            = LittleEndian.getShort(data, 0x4 + offset);
        field_4_formatFlags             = LittleEndian.getShort(data, 0x6 + offset);

    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("[DataFormat]\n");

        buffer.append("    .pointNumber          = ")
            .append("0x")
            .append(HexDump.toHex((short)getPointNumber()))
            .append(" (").append(getPointNumber()).append(" )\n");

        buffer.append("    .seriesIndex          = ")
            .append("0x")
            .append(HexDump.toHex((short)getSeriesIndex()))
            .append(" (").append(getSeriesIndex()).append(" )\n");

        buffer.append("    .seriesNumber         = ")
            .append("0x")
            .append(HexDump.toHex((short)getSeriesNumber()))
            .append(" (").append(getSeriesNumber()).append(" )\n");

        buffer.append("    .formatFlags          = ")
            .append("0x")
            .append(HexDump.toHex((short)getFormatFlags()))
            .append(" (").append(getFormatFlags()).append(" )\n");
        buffer.append("         .useExcel4Colors          = ").append(isUseExcel4Colors     ()).append('\n');

        buffer.append("[/DataFormat]\n");
        return buffer.toString();
    }

    public int serialize(int offset, byte[] data)
    {
        LittleEndian.putShort(data, 0 + offset, sid);
        LittleEndian.putShort(data, 2 + offset, (short)(getRecordSize() - 4));

        LittleEndian.putShort(data, 4 + offset, field_1_pointNumber);
        LittleEndian.putShort(data, 6 + offset, field_2_seriesIndex);
        LittleEndian.putShort(data, 8 + offset, field_3_seriesNumber);
        LittleEndian.putShort(data, 10 + offset, field_4_formatFlags);

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
     * Get the point number field for the DataFormat record.
     */
    public short getPointNumber()
    {
        return field_1_pointNumber;
    }

    /**
     * Set the point number field for the DataFormat record.
     */
    public void setPointNumber(short field_1_pointNumber)
    {
        this.field_1_pointNumber = field_1_pointNumber;
    }

    /**
     * Get the series index field for the DataFormat record.
     */
    public short getSeriesIndex()
    {
        return field_2_seriesIndex;
    }

    /**
     * Set the series index field for the DataFormat record.
     */
    public void setSeriesIndex(short field_2_seriesIndex)
    {
        this.field_2_seriesIndex = field_2_seriesIndex;
    }

    /**
     * Get the series number field for the DataFormat record.
     */
    public short getSeriesNumber()
    {
        return field_3_seriesNumber;
    }

    /**
     * Set the series number field for the DataFormat record.
     */
    public void setSeriesNumber(short field_3_seriesNumber)
    {
        this.field_3_seriesNumber = field_3_seriesNumber;
    }

    /**
     * Get the format flags field for the DataFormat record.
     */
    public short getFormatFlags()
    {
        return field_4_formatFlags;
    }

    /**
     * Set the format flags field for the DataFormat record.
     */
    public void setFormatFlags(short field_4_formatFlags)
    {
        this.field_4_formatFlags = field_4_formatFlags;
    }

    /**
     * Sets the use excel 4 colors field value.
     * set true to use excel 4 colors.
     */
    public void setUseExcel4Colors(boolean value)
    {
        field_4_formatFlags = useExcel4Colors.setShortBoolean(field_4_formatFlags, value);
    }

    /**
     * set true to use excel 4 colors.
     * @return  the use excel 4 colors field value.
     */
    public boolean isUseExcel4Colors()
    {
        return useExcel4Colors.isSet(field_4_formatFlags);
    }






