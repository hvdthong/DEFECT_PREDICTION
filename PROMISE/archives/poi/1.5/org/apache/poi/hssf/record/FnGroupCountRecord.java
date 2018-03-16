package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndian;

/**
 * Title: Function Group Count Record<P>
 * Description:  Number of built in function groups in the current version of the
 *               Spreadsheet (probably only used on Windoze)<P>
 * REFERENCE:  PG 315 Microsoft Excel 97 Developer's Kit (ISBN: 1-57231-498-2)<P>
 * @author Andrew C. Oliver (acoliver at apache dot org)
 * @version 2.0-pre
 */

public class FnGroupCountRecord
    extends Record
{
    public final static short sid   = 0x9c;

    /**
     * suggested default (14 dec)
     */

    public final static short COUNT = 14;
    private short             field_1_count;

    public FnGroupCountRecord()
    {
    }

    /**
     * Constructs a FnGroupCount record and sets its fields appropriately.
     *
     * @param id     id must be 0x9c or an exception will be throw upon validation
     * @param size  the size of the data area of the record
     * @param data  data of the record (should not contain sid/len)
     */

    public FnGroupCountRecord(short id, short size, byte [] data)
    {
        super(id, size, data);
    }

    /**
     * Constructs a FnGroupCount record and sets its fields appropriately.
     *
     * @param id     id must be 0x9c or an exception will be throw upon validation
     * @param size  the size of the data area of the record
     * @param data  data of the record (should not contain sid/len)
     * @param offset of the record's data
     */

    public FnGroupCountRecord(short id, short size, byte [] data, int offset)
    {
        super(id, size, data, offset);
    }

    protected void validateSid(short id)
    {
        if (id != sid)
        {
            throw new RecordFormatException("NOT A FNGROUPCOUNT RECORD");
        }
    }

    protected void fillFields(byte [] data, short size, int offset)
    {
        field_1_count = LittleEndian.getShort(data, 0 + offset);
    }

    /**
     * set the number of built-in functions
     *
     * @param count - number of functions
     */

    public void setCount(short count)
    {
        field_1_count = count;
    }

    /**
     * get the number of built-in functions
     *
     * @return number of built-in functions
     */

    public short getCount()
    {
        return field_1_count;
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("[FNGROUPCOUNT]\n");
        buffer.append("    .count            = ").append(getCount())
            .append("\n");
        buffer.append("[/FNGROUPCOUNT]\n");
        return buffer.toString();
    }

    public int serialize(int offset, byte [] data)
    {
        LittleEndian.putShort(data, 0 + offset, sid);
        LittleEndian.putShort(data, 2 + offset,
        LittleEndian.putShort(data, 4 + offset, getCount());
        return getRecordSize();
    }

    public int getRecordSize()
    {
        return 6;
    }

    public short getSid()
    {
        return this.sid;
    }
}
