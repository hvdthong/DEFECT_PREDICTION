package org.apache.poi.hssf.record.formula;

import org.apache.poi.util.LittleEndian;
import org.apache.poi.hssf.model.Workbook;

/**
 *
 * @author  aviks
 */

public class NameXPtg extends Ptg
{
    public final static short sid  = 0x39;
    private final static int  SIZE = 7;


    private NameXPtg() {
    }

    /** Creates new NamePtg */

    public NameXPtg(String name)
    {
    }

    /** Creates new NamePtg */

    public NameXPtg(byte[] data, int offset)
    {
        offset++;
        field_1_ixals        = LittleEndian.getShort(data, offset);
        field_2_ilbl        = LittleEndian.getShort(data, offset + 2);
        field_3_reserved = LittleEndian.getShort(data, offset +4);
        
    }

    public void writeBytes(byte [] array, int offset)
    {
        array[ offset + 0 ] = (byte)(sid + ptgClass);
        LittleEndian.putShort(array, offset + 1, field_1_ixals);
        LittleEndian.putShort(array,offset+3, field_2_ilbl);
        LittleEndian.putShort(array, offset + 5, field_3_reserved);
    }

    public int getSize()
    {
        return SIZE;
    }

    public String toFormulaString(Workbook book)
    {
        return "NO IDEA - NAME";
    }
    
    public byte getDefaultOperandClass() {return Ptg.CLASS_VALUE;}

    public Object clone() {
      NameXPtg ptg = new NameXPtg();
      ptg.field_1_ixals = field_1_ixals;
      ptg.field_3_reserved = field_3_reserved;
      ptg.field_2_ilbl = field_2_ilbl;
      ptg.setClass(ptgClass);
      return ptg;
    }
}
