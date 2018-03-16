package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndian;

/**
 * Title:        Save Recalc Record <P>
 * Description:  defines whether to recalculate before saving (set to true)<P>
 * REFERENCE:  PG 381 Microsoft Excel 97 Developer's Kit (ISBN: 1-57231-498-2)<P>
 * @author Andrew C. Oliver (acoliver at apache dot org)
 * @version 2.0-pre
 */

public class SaveRecalcRecord
    extends Record
{
    public final static short sid = 0x5f;
    private short             field_1_recalc;

    public SaveRecalcRecord()
    {
    }

    /**
     * Constructs an SaveRecalc record and sets its fields appropriately.
     *
     * @param id     id must be 0x5f or an exception will be throw upon validation
     * @param size  the size of the data area of the record
     * @param data  data of the record (should not contain sid/len)
     */

    public SaveRecalcRecord(short id, short size, byte [] data)
    {
        super(id, size, data);
    }

    /**
     * Constructs an SaveRecalc record and sets its fields appropriately.
     *
     * @param id     id must be 0x5f or an exception will be throw upon validation
     * @param size  the size of the data area of the record
     * @param data  data of the record (should not contain sid/len)
     * @param offset of the the data
     */

    public SaveRecalcRecord(short id, short size, byte [] data, int offset)
    {
        super(id, size, data, offset);
    }

    protected void validateSid(short id)
    {
        if (id != sid)
        {
            throw new RecordFormatException("NOT A Save Recalc RECORD");
        }
    }

    protected void fillFields(byte [] data, short size, int offset)
    {
        field_1_recalc = LittleEndian.getShort(data, 0 + offset);
    }

    /**
     * set whether to recalculate formulas/etc before saving or not
     * @param recalc - whether to recalculate or not
     */

    public void setRecalc(boolean recalc)
    {
        field_1_recalc = ( short ) ((recalc == true) ? 1
                                                     : 0);
    }

    /**
     * get whether to recalculate formulas/etc before saving or not
     * @return recalc - whether to recalculate or not
     */

    public boolean getRecalc()
    {
        return (field_1_recalc == 1);
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("[SAVERECALC]\n");
        buffer.append("    .recalc         = ").append(getRecalc())
            .append("\n");
        buffer.append("[/SAVERECALC]\n");
        return buffer.toString();
    }

    public int serialize(int offset, byte [] data)
    {
        LittleEndian.putShort(data, 0 + offset, sid);
        LittleEndian.putShort(data, 2 + offset, ( short ) 0x2);
        LittleEndian.putShort(data, 4 + offset, field_1_recalc);
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
