package org.apache.poi.hssf.record.formula;

import org.apache.poi.util.LittleEndian;
import org.apache.poi.hssf.model.Workbook;

/**
 * Integer (short intger)
 * Stores a (java) short value in a formula
 * @author  Andrew C. Oliver (acoliver at apache dot org)
 * @author Jason Height (jheight at chariot dot net dot au)
 */

public class IntPtg
    extends Ptg
{
    public final static int  SIZE = 3;
    public final static byte sid  = 0x1e;
    private short            field_1_value;

    private String val;
    private int strlen = 0;
  
    private IntPtg() {
    }

    public IntPtg(byte [] data, int offset)
    {
        setValue(LittleEndian.getShort(data, offset + 1));
    }
    
    
    public IntPtg(String formulaToken) {
        setValue(Short.parseShort(formulaToken));
    }

    public void setValue(short value)
    {
        field_1_value = value;
    }

    public short getValue()
    {
        return field_1_value;
    }

    public void writeBytes(byte [] array, int offset)
    {
        array[ offset + 0 ] = sid;
        LittleEndian.putShort(array, offset + 1, getValue());
    }

    public int getSize()
    {
        return SIZE;
    }

    public String toFormulaString(Workbook book)
    {
        return "" + getValue();
    }
 public byte getDefaultOperandClass() {return Ptg.CLASS_VALUE;}   

   public Object clone() {
     IntPtg ptg = new IntPtg();
     ptg.field_1_value = field_1_value;
     return ptg;
   }
}
