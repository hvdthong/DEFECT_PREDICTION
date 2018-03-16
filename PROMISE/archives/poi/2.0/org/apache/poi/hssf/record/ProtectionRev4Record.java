package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndian;

/**
 * Title:        Protection Revision 4 Record<P>
 * Description:  describes whether this is a protected shared/tracked workbook<P>
 *  ( HSSF does not support encryption because we don't feel like going to jail ) <P>
 * REFERENCE:  PG 373 Microsoft Excel 97 Developer's Kit (ISBN: 1-57231-498-2)<P>
 * @author Andrew C. Oliver (acoliver at apache dot org)
 * @version 2.0-pre
 */

public class ProtectionRev4Record
    extends Record
{
    public final static short sid = 0x1af;
    private short             field_1_protect;

    public ProtectionRev4Record()
    {
    }

    /**
     * Constructs a ProtectionRev4 record and sets its fields appropriately.
     *
     * @param id     id must be 0x1af or an exception will be throw upon validation
     * @param size  the size of the data area of the record
     * @param data  data of the record (should not contain sid/len)
     */

    public ProtectionRev4Record(short id, short size, byte [] data)
    {
        super(id, size, data);
    }

    /**
     * Constructs a ProtectionRev4 record and sets its fields appropriately.
     *
     * @param id     id must be 0x1af or an exception will be throw upon validation
     * @param size  the size of the data area of the record
     * @param data  data of the record (should not contain sid/len)
     * @param offset of the data
     */

    public ProtectionRev4Record(short id, short size, byte [] data,
                                int offset)
    {
        super(id, size, data, offset);
    }

    protected void validateSid(short id)
    {
        if (id != sid)
        {
            throw new RecordFormatException("NOT A PROTECTION REV 4 RECORD");
        }
    }

    protected void fillFields(byte [] data, short size, int offset)
    {
        field_1_protect = LittleEndian.getShort(data, 0 + offset);
    }

    /**
     * set whether the this is protected shared/tracked workbook or not
     * @param protect  whether to protect the workbook or not
     */

    public void setProtect(boolean protect)
    {
        if (protect)
        {
            field_1_protect = 1;
        }
        else
        {
            field_1_protect = 0;
        }
    }

    /**
     * get whether the this is protected shared/tracked workbook or not
     * @return whether to protect the workbook or not
     */

	public boolean getProtect()
	{
		return (field_1_protect == 1);
	}

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("[PROT4REV]\n");
	    buffer.append("    .protect         = ").append(getProtect())
            .append("\n");
        buffer.append("[/PROT4REV]\n");
        return buffer.toString();
    }

    public int serialize(int offset, byte [] data)
    {
        LittleEndian.putShort(data, 0 + offset, sid);
        LittleEndian.putShort(data, 2 + offset,
        LittleEndian.putShort(data, 4 + offset, field_1_protect);
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
