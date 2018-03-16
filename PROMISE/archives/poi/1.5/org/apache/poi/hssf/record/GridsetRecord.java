package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndian;

/**
 * Title:        Gridset Record.<P>
 * Description:  flag denoting whether the user specified that gridlines are used when
 *               printing.<P>
 * REFERENCE:  PG 320 Microsoft Excel 97 Developer's Kit (ISBN: 1-57231-498-2)<P>
 *
 * @author Andrew C. Oliver (acoliver at apache dot org)
 * @author  Glen Stampoultzis (glens at apache.org)
 *
 * @version 2.0-pre
 */

public class GridsetRecord
    extends Record
{
    public final static short sid = 0x82;
    public short              field_1_gridset_flag;

    public GridsetRecord()
    {
    }

    /**
     * Constructs a GridSet record and sets its fields appropriately.
     *
     * @param id     id must be 0x82 or an exception will be throw upon validation
     * @param size  the size of the data area of the record
     * @param data  data of the record (should not contain sid/len)
     */

    public GridsetRecord(short id, short size, byte [] data)
    {
        super(id, size, data);
    }

    /**
     * Constructs a GridSet record and sets its fields appropriately.
     *
     * @param id     id must be 0x82 or an exception will be throw upon validation
     * @param size  the size of the data area of the record
     * @param data  data of the record (should not contain sid/len)
     * @param offset of the record's data
     */

    public GridsetRecord(short id, short size, byte [] data, int offset)
    {
        super(id, size, data, offset);
    }

    protected void validateSid(short id)
    {
        if (id != sid)
        {
            throw new RecordFormatException("NOT A Gridset RECORD");
        }
    }

    protected void fillFields(byte [] data, short size, int offset)
    {
        field_1_gridset_flag = LittleEndian.getShort(data, 0 + offset);
    }

    /**
     * set whether gridlines are visible when printing
     *
     * @param gridset - <b>true</b> if no gridlines are print, <b>false</b> if gridlines are not print.
     */

    public void setGridset(boolean gridset)
    {
        if (gridset == true)
        {
            field_1_gridset_flag = 1;
        }
        else
        {
            field_1_gridset_flag = 0;
        }
    }

    /**
     * get whether the gridlines are shown during printing.
     *
     * @return gridset - true if gridlines are NOT printed, false if they are.
     */

    public boolean getGridset()
    {
        return (field_1_gridset_flag == 1);
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("[GRIDSET]\n");
        buffer.append("    .gridset        = ").append(getGridset())
            .append("\n");
        buffer.append("[/GRIDSET]\n");
        return buffer.toString();
    }

    public int serialize(int offset, byte [] data)
    {
        LittleEndian.putShort(data, 0 + offset, sid);
        LittleEndian.putShort(data, 2 + offset, ( short ) 0x2);
        LittleEndian.putShort(data, 4 + offset, field_1_gridset_flag);
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
