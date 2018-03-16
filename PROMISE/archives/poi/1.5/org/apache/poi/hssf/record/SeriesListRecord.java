package org.apache.poi.hssf.record;



import org.apache.poi.util.BitField;
import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.StringUtil;
import org.apache.poi.util.HexDump;

/**
 * The series list record defines the series displayed as an overlay to the main chart record.
 * NOTE: This source is automatically generated please do not modify this file.  Either subclass or
 *       remove the record in src/records/definitions.

 * @author Glen Stampoultzis (glens at apache.org)
 */
public class SeriesListRecord
    extends Record
{
    public final static short      sid                             = 0x1016;
    private  short[]    field_1_seriesNumbers;


    public SeriesListRecord()
    {

    }

    /**
     * Constructs a SeriesList record and sets its fields appropriately.
     *
     * @param id    id must be 0x1016 or an exception
     *              will be throw upon validation
     * @param size  size the size of the data area of the record
     * @param data  data of the record (should not contain sid/len)
     */

    public SeriesListRecord(short id, short size, byte [] data)
    {
        super(id, size, data);
    }

    /**
     * Constructs a SeriesList record and sets its fields appropriately.
     *
     * @param id    id must be 0x1016 or an exception
     *              will be throw upon validation
     * @param size  size the size of the data area of the record
     * @param data  data of the record (should not contain sid/len)
     * @param offset of the record's data
     */

    public SeriesListRecord(short id, short size, byte [] data, int offset)
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
            throw new RecordFormatException("Not a SeriesList record");
        }
    }

    protected void fillFields(byte [] data, short size, int offset)
    {
        field_1_seriesNumbers           = LittleEndian.getShortArray(data, 0x0 + offset);

    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("[SeriesList]\n");

        buffer.append("    .seriesNumbers        = ")
            .append(" (").append(getSeriesNumbers()).append(" )\n");

        buffer.append("[/SeriesList]\n");
        return buffer.toString();
    }

    public int serialize(int offset, byte[] data)
    {
        LittleEndian.putShort(data, 0 + offset, sid);
        LittleEndian.putShort(data, 2 + offset, (short)(getRecordSize() - 4));

        LittleEndian.putShortArray(data, 4 + offset, field_1_seriesNumbers);

        return getRecordSize();
    }

    /**
     * Size of record (exluding 4 byte header)
     */
    public int getRecordSize()
    {
        return 4 + field_1_seriesNumbers.length * 2 + 2;
    }

    public short getSid()
    {
        return this.sid;
    }


    /**
     * Get the series numbers field for the SeriesList record.
     */
    public short[] getSeriesNumbers()
    {
        return field_1_seriesNumbers;
    }

    /**
     * Set the series numbers field for the SeriesList record.
     */
    public void setSeriesNumbers(short[] field_1_seriesNumbers)
    {
        this.field_1_seriesNumbers = field_1_seriesNumbers;
    }






