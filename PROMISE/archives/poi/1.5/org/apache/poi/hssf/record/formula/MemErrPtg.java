package org.apache.poi.hssf.record.formula;

import org.apache.poi.util.LittleEndian;

/**
 *
 * @author  andy
 */

public class MemErrPtg
    extends Ptg
{
    public final static short sid  = 0x27;
    private final static int  SIZE = 7;
    private int               field_1_reserved;
    private short             field_2_subex_len;

    /** Creates new MemErrPtg */

    public MemErrPtg()
    {
    }

    public MemErrPtg(byte [] data, int offset)
    {
        field_1_reserved  = LittleEndian.getInt(data, 0);
        field_2_subex_len = LittleEndian.getShort(data, 4);
    }

    public void setReserved(int res)
    {
        field_1_reserved = res;
    }

    public int getReserved()
    {
        return field_1_reserved;
    }

    public void setSubexpressionLength(short subexlen)
    {
        field_2_subex_len = subexlen;
    }

    public short getSubexpressionLength()
    {
        return field_2_subex_len;
    }

    public void writeBytes(byte [] array, int offset)
    {
    }

    public int getSize()
    {
        return SIZE;
    }

    public String toFormulaString()
    {
        return "ERR#";
    }
}
