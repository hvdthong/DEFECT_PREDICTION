package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndian;

/**
 * Title: Interface Header Record<P>
 * Description: Defines the beginning of Interface records (MMS)<P>
 * REFERENCE:  PG 324 Microsoft Excel 97 Developer's Kit (ISBN: 1-57231-498-2)<P>
 * @author Andrew C. Oliver (acoliver at apache dot org)
 * @version 2.0-pre
 */

public class InterfaceHdrRecord
    extends Record
{
    public final static short sid = 0xe1;

    /**
     * suggested (and probably correct) default
     */

    public final static short CODEPAGE = ( short ) 0x4b0;

    public InterfaceHdrRecord()
    {
    }

    /**
     * Constructs an Codepage record and sets its fields appropriately.
     *
     * @param id     id must be 0xe1 or an exception will be throw upon validation
     * @param size  the size of the data area of the record
     * @param data  data of the record (should not contain sid/len)
     */

    public InterfaceHdrRecord(short id, short size, byte [] data)
    {
        super(id, size, data);
    }

    /**
     * Constructs an Codepage record and sets its fields appropriately.
     *
     * @param id     id must be 0xe1 or an exception will be throw upon validation
     * @param size  the size of the data area of the record
     * @param data  data of the record (should not contain sid/len)
     * @param offset of the record's data
     */

    public InterfaceHdrRecord(short id, short size, byte [] data, int offset)
    {
        super(id, size, data, offset);
    }

    protected void validateSid(short id)
    {
        if (id != sid)
        {
            throw new RecordFormatException("NOT A INTERFACEHDR RECORD");
        }
    }

    protected void fillFields(byte [] data, short size, int offset)
    {
        field_1_codepage = LittleEndian.getShort(data, 0 + offset);
    }

    /**
     * set the codepage for the file
     *
     * @param cp - the codepage
     * @see #CODEPAGE
     */

    public void setCodepage(short cp)
    {
        field_1_codepage = cp;
    }

    /**
     * get the codepage for the file
     *
     * @return the codepage
     * @see #CODEPAGE
     */

    public short getCodepage()
    {
        return field_1_codepage;
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("[INTERFACEHDR]\n");
        buffer.append("    .codepage        = ")
            .append(Integer.toHexString(getCodepage())).append("\n");
        buffer.append("[/INTERFACEHDR]\n");
        return buffer.toString();
    }

    public int serialize(int offset, byte [] data)
    {
        LittleEndian.putShort(data, 0 + offset, sid);
        LittleEndian.putShort(data, 2 + offset,
        LittleEndian.putShort(data, 4 + offset, getCodepage());
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
