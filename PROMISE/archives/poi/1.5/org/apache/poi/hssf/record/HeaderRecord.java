package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.StringUtil;

/**
 * Title:        Header Record<P>
 * Description:  Specifies a header for a sheet<P>
 * REFERENCE:  PG 321 Microsoft Excel 97 Developer's Kit (ISBN: 1-57231-498-2)<P>
 * @author Andrew C. Oliver (acoliver at apache dot org)
 * @author Shawn Laubach (laubach@acm.org) Modified 3/14/02
 * @version 2.0-pre
 */

public class HeaderRecord
    extends Record
{
    public final static short sid = 0x14;
    private byte              field_1_header_len;
    private String            field_2_header;

    public HeaderRecord()
    {
    }

    /**
     * Constructs an Header record and sets its fields appropriately.
     *
     * @param id     id must be 0x14 or an exception will be throw upon validation
     * @param size  the size of the data area of the record
     * @param data  data of the record (should not contain sid/len)
     */

    public HeaderRecord(short id, short size, byte [] data)
    {
        super(id, size, data);
    }

    /**
     * Constructs an Header record and sets its fields appropriately.
     *
     * @param id     id must be 0x14 or an exception will be throw upon validation
     * @param size  the size of the data area of the record
     * @param data  data of the record (should not contain sid/len)
     * @param offset of the record's data
     */

    public HeaderRecord(short id, short size, byte [] data, int offset)
    {
        super(id, size, data, offset);
    }

    protected void validateSid(short id)
    {
        if (id != sid)
        {
            throw new RecordFormatException("NOT A HEADERRECORD");
        }
    }

    protected void fillFields(byte [] data, short size, int offset)
    {
        if (size > 0)
        {
            field_1_header_len = data[ 0 + offset ];
                                            LittleEndian.ubyteToInt(field_1_header_len));
        }
    }

    /**
     * set the length of the header string
     *
     * @param len  length of the header string
     * @see #setHeader(String)
     */

    public void setHeaderLength(byte len)
    {
        field_1_header_len = len;
    }

    /**
     * set the header string
     *
     * @param header string to display
     * @see #setHeaderLength(byte)
     */

    public void setHeader(String header)
    {
        field_2_header = header;
    }

    /**
     * get the length of the header string
     *
     * @return length of the header string
     * @see #getHeader()
     */

    public short getHeaderLength()
    {
    }

    /**
     * get the header string
     *
     * @return header string to display
     * @see #getHeaderLength()
     */

    public String getHeader()
    {
        return field_2_header;
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("[HEADER]\n");
        buffer.append("    .length         = ").append(getHeaderLength())
            .append("\n");
        buffer.append("    .header         = ").append(getHeader())
            .append("\n");
        buffer.append("[/HEADER]\n");
        return buffer.toString();
    }

    public int serialize(int offset, byte [] data)
    {
        int len = 4;

        if (getHeaderLength() != 0)
        {
        }
        LittleEndian.putShort(data, 0 + offset, sid);
        LittleEndian.putShort(data, 2 + offset,
                              ( short ) ((len - 4) + getHeaderLength()));

        if (getHeaderLength() > 0)
        {
            data[ 4 + offset ] = (byte)getHeaderLength();
        }
        return getRecordSize();
    }

    public int getRecordSize()
    {
        int retval = 4;

        if (getHeaderLength() != 0)
        {
        }
        retval += getHeaderLength();
        return retval;
    }

    public short getSid()
    {
        return this.sid;
    }
}
