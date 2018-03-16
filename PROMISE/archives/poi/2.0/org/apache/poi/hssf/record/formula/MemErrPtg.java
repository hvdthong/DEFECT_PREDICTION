package org.apache.poi.hssf.record.formula;

import org.apache.poi.util.LittleEndian;
import org.apache.poi.hssf.model.Workbook;

/**
 *
 * @author  andy
 * @author Jason Height (jheight at chariot dot net dot au)
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

    public String toFormulaString(Workbook book)
    {
        return "ERR#";
    }
    public byte getDefaultOperandClass() {return Ptg.CLASS_VALUE;}

    public Object clone() {
      MemErrPtg ptg = new MemErrPtg();
      ptg.field_1_reserved = field_1_reserved;
      ptg.field_2_subex_len = field_2_subex_len;
      return ptg;
    }
}
