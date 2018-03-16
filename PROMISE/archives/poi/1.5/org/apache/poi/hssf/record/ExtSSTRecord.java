package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndian;

import java.util.ArrayList;

/**
 * Title:        Extended Static String Table<P>
 * Description:  I really don't understand this thing... its supposed to be "a hash
 *               table for optimizing external copy operations"  --
 *<P>
 *               This sounds like a job for Marc "BitMaster" Johnson aka the
 *               "Hawaiian Master Chef".<P>
 * REFERENCE:  PG 313 Microsoft Excel 97 Developer's Kit (ISBN: 1-57231-498-2)<P>
 * @author Andrew C. Oliver (acoliver at apache dot org)
 * @version 2.0-pre
 * @see org.apache.poi.hssf.record.ExtSSTInfoSubRecord
 */

public class ExtSSTRecord
    extends Record
{
    public final static short sid = 0xff;
    private short             field_1_strings_per_bucket;
    private ArrayList         field_2_sst_info;

    public ExtSSTRecord()
    {
        field_2_sst_info = new ArrayList();
    }

    /**
     * Constructs a EOFRecord record and sets its fields appropriately.
     *
     * @param id     id must be 0xff or an exception will be throw upon validation
     * @param size  the size of the data area of the record
     * @param data  data of the record (should not contain sid/len)
     */

    public ExtSSTRecord(short id, short size, byte [] data)
    {
        super(id, size, data);
    }

    /**
     * Constructs a EOFRecord record and sets its fields appropriately.
     *
     * @param id     id must be 0xff or an exception will be throw upon validation
     * @param size  the size of the data area of the record
     * @param data  data of the record (should not contain sid/len)
     * @param offset of the record's data
     */

    public ExtSSTRecord(short id, short size, byte [] data, int offset)
    {
        super(id, size, data, offset);
    }

    protected void validateSid(short id)
    {
        if (id != sid)
        {
            throw new RecordFormatException("NOT An EXTSST RECORD");
        }
    }

    protected void fillFields(byte [] data, short size, int offset)
    {
        field_2_sst_info           = new ArrayList();
        field_1_strings_per_bucket = LittleEndian.getShort(data, 0 + offset);
        for (int k = 2; k < ((data.length - offset) - size); k += 8)
        {
            byte[] tempdata = new byte[ 8 + offset ];

            System.arraycopy(data, k, tempdata, 0, 8);
            ExtSSTInfoSubRecord rec = new ExtSSTInfoSubRecord(( short ) 0,
                                          ( short ) 8, tempdata);

            field_2_sst_info.add(rec);
        }
    }

    public void setNumStringsPerBucket(short numStrings)
    {
        field_1_strings_per_bucket = numStrings;
    }

    public void addInfoRecord(ExtSSTInfoSubRecord rec)
    {
        field_2_sst_info.add(rec);
    }

    public short getNumStringsPerBucket()
    {
        return field_1_strings_per_bucket;
    }

    public int getNumInfoRecords()
    {
        return field_2_sst_info.size();
    }

    public ExtSSTInfoSubRecord getInfoRecordAt(int elem)
    {
        return ( ExtSSTInfoSubRecord ) field_2_sst_info.get(elem);
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("[EXTSST]\n");
        buffer.append("    .dsst           = ")
            .append(Integer.toHexString(getNumStringsPerBucket()))
            .append("\n");
        buffer.append("    .numInfoRecords = ").append(getNumInfoRecords())
            .append("\n");
        for (int k = 0; k < getNumInfoRecords(); k++)
        {
            buffer.append("    .inforecord     = ").append(k).append("\n");
            buffer.append("    .streampos      = ")
                .append(Integer
                .toHexString(getInfoRecordAt(k).getStreamPos())).append("\n");
            buffer.append("    .sstoffset      = ")
                .append(Integer
                .toHexString(getInfoRecordAt(k).getBucketSSTOffset()))
                    .append("\n");
        }
        buffer.append("[/EXTSST]\n");
        return buffer.toString();
    }

    public int serialize(int offset, byte [] data)
    {
        LittleEndian.putShort(data, 0 + offset, sid);

        LittleEndian.putShort(data, 2 + offset, ( short ) (2 + (0x3fa - 2)));
        int pos = 4;

        for (int k = 0; k < getNumInfoRecords(); k++)
        {
            System.arraycopy(getInfoRecordAt(k).serialize(), 0, data,
                             pos + offset, 8);
        }
        return getRecordSize();
    }

    public int getRecordSize()
    {
        return 6 + 0x3fa - 2;
    }

    public short getSid()
    {
        return this.sid;
    }
}
