package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndian;

/**
 * Title:        Delta Record<P>
 * Description:  controls the accuracy of the calculations<P>
 * REFERENCE:  PG 303 Microsoft Excel 97 Developer's Kit (ISBN: 1-57231-498-2)<P>
 * @author Andrew C. Oliver (acoliver at apache dot org)
 * @version 2.0-pre
 */

public class DeltaRecord
    extends Record
{
    public final static short  sid           = 0x10;

    private double             field_1_max_change;

    public DeltaRecord()
    {
    }

    /**
     * Constructs a Delta record and sets its fields appropriately.
     *
     * @param id     id must be 0x10 or an exception will be throw upon validation
     * @param size  the size of the data area of the record
     * @param data  data of the record (should not contain sid/len)
     */

    public DeltaRecord(short id, short size, byte [] data)
    {
        super(id, size, data);
    }

    /**
     * Constructs a Delta record and sets its fields appropriately.
     *
     * @param id     id must be 0x10 or an exception will be throw upon validation
     * @param size  the size of the data area of the record
     * @param data  data of the record (should not contain sid/len)
     * @param offset of record data
     */

    public DeltaRecord(short id, short size, byte [] data, int offset)
    {
        super(id, size, data, offset);
    }

    protected void validateSid(short id)
    {
        if (id != sid)
        {
            throw new RecordFormatException("NOT A DELTA RECORD");
        }
    }

    protected void fillFields(byte [] data, short size, int offset)
    {
        field_1_max_change = LittleEndian.getDouble(data, 0 + offset);
    }

    /**
     * set the maximum change
     * @param maxChange - maximum rounding error
     */

    public void setMaxChange(double maxChange)
    {
        field_1_max_change = maxChange;
    }

    /**
     * get the maximum change
     * @return maxChange - maximum rounding error
     */

    public double getMaxChange()
    {
        return field_1_max_change;
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("[DELTA]\n");
        buffer.append("    .maxchange      = ").append(getMaxChange())
            .append("\n");
        buffer.append("[/DELTA]\n");
        return buffer.toString();
    }

    public int serialize(int offset, byte [] data)
    {
        LittleEndian.putShort(data, 0 + offset, sid);
        LittleEndian.putShort(data, 2 + offset, ( short ) 0x8);
        LittleEndian.putDouble(data, 4 + offset, getMaxChange());
        return getRecordSize();
    }

    public int getRecordSize()
    {
        return 12;
    }

    public short getSid()
    {
        return this.sid;
    }
}
