package org.apache.poi.hssf.record;



import org.apache.poi.util.BitField;
import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.StringUtil;
import org.apache.poi.util.HexDump;

/**
 * The font index record indexes into the font table for the text record.
 * NOTE: This source is automatically generated please do not modify this file.  Either subclass or
 *       remove the record in src/records/definitions.

 * @author Glen Stampoultzis (glens at apache.org)
 */
public class FontIndexRecord
    extends Record
{
    public final static short      sid                             = 0x1026;
    private  short      field_1_fontIndex;


    public FontIndexRecord()
    {

    }

    /**
     * Constructs a FontIndex record and sets its fields appropriately.
     *
     * @param id    id must be 0x1026 or an exception
     *              will be throw upon validation
     * @param size  size the size of the data area of the record
     * @param data  data of the record (should not contain sid/len)
     */

    public FontIndexRecord(short id, short size, byte [] data)
    {
        super(id, size, data);
    }

    /**
     * Constructs a FontIndex record and sets its fields appropriately.
     *
     * @param id    id must be 0x1026 or an exception
     *              will be throw upon validation
     * @param size  size the size of the data area of the record
     * @param data  data of the record (should not contain sid/len)
     * @param offset of the record's data
     */

    public FontIndexRecord(short id, short size, byte [] data, int offset)
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
            throw new RecordFormatException("Not a FontIndex record");
        }
    }

    protected void fillFields(byte [] data, short size, int offset)
    {
        field_1_fontIndex               = LittleEndian.getShort(data, 0x0 + offset);

    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("[FontIndex]\n");

        buffer.append("    .fontIndex            = ")
            .append("0x")
            .append(HexDump.toHex((short)getFontIndex()))
            .append(" (").append(getFontIndex()).append(" )\n");

        buffer.append("[/FontIndex]\n");
        return buffer.toString();
    }

    public int serialize(int offset, byte[] data)
    {
        LittleEndian.putShort(data, 0 + offset, sid);
        LittleEndian.putShort(data, 2 + offset, (short)(getRecordSize() - 4));

        LittleEndian.putShort(data, 4 + offset, field_1_fontIndex);

        return getRecordSize();
    }

    /**
     * Size of record (exluding 4 byte header)
     */
    public int getRecordSize()
    {
        return 4 + 2;
    }

    public short getSid()
    {
        return this.sid;
    }


    /**
     * Get the font index field for the FontIndex record.
     */
    public short getFontIndex()
    {
        return field_1_fontIndex;
    }

    /**
     * Set the font index field for the FontIndex record.
     */
    public void setFontIndex(short field_1_fontIndex)
    {
        this.field_1_fontIndex = field_1_fontIndex;
    }






