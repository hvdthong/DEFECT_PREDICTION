package org.apache.poi.hssf.record;



import org.apache.poi.util.*;

/**
 * The units record describes units.
 * NOTE: This source is automatically generated please do not modify this file.  Either subclass or
 *       remove the record in src/records/definitions.

 * @author Glen Stampoultzis (glens at apache.org)
 */
public class UnitsRecord
    extends Record
{
    public final static short      sid                             = 0x1001;
    private  short      field_1_units;


    public UnitsRecord()
    {

    }

    /**
     * Constructs a Units record and sets its fields appropriately.
     *
     * @param id    id must be 0x1001 or an exception
     *              will be throw upon validation
     * @param size  size the size of the data area of the record
     * @param data  data of the record (should not contain sid/len)
     */

    public UnitsRecord(short id, short size, byte [] data)
    {
        super(id, size, data);
    
    }

    /**
     * Constructs a Units record and sets its fields appropriately.
     *
     * @param id    id must be 0x1001 or an exception
     *              will be throw upon validation
     * @param size  size the size of the data area of the record
     * @param data  data of the record (should not contain sid/len)
     * @param offset of the record's data
     */

    public UnitsRecord(short id, short size, byte [] data, int offset)
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
            throw new RecordFormatException("Not a Units record");
        }
    }

    protected void fillFields(byte [] data, short size, int offset)
    {

        int pos = 0;
        field_1_units                  = LittleEndian.getShort(data, pos + 0x0 + offset);

    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("[UNITS]\n");
        buffer.append("    .units                = ")
            .append("0x").append(HexDump.toHex(  getUnits ()))
            .append(" (").append( getUnits() ).append(" )");
        buffer.append(System.getProperty("line.separator")); 

        buffer.append("[/UNITS]\n");
        return buffer.toString();
    }

    public int serialize(int offset, byte[] data)
    {
        int pos = 0;

        LittleEndian.putShort(data, 0 + offset, sid);
        LittleEndian.putShort(data, 2 + offset, (short)(getRecordSize() - 4));

        LittleEndian.putShort(data, 4 + offset + pos, field_1_units);

        return getRecordSize();
    }

    /**
     * Size of record (exluding 4 byte header)
     */
    public int getRecordSize()
    {
        return 4  + 2;
    }

    public short getSid()
    {
        return this.sid;
    }

    public Object clone() {
        UnitsRecord rec = new UnitsRecord();
    
        rec.field_1_units = field_1_units;
        return rec;
    }




    /**
     * Get the units field for the Units record.
     */
    public short getUnits()
    {
        return field_1_units;
    }

    /**
     * Set the units field for the Units record.
     */
    public void setUnits(short field_1_units)
    {
        this.field_1_units = field_1_units;
    }






