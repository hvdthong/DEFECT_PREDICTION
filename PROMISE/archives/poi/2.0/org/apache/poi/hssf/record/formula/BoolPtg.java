package org.apache.poi.hssf.record.formula;

import org.apache.poi.util.LittleEndian;
import org.apache.poi.hssf.model.Workbook;

/**
 * Boolean (boolean)
 * Stores a (java) boolean value in a formula.
 * @author Paul Krause (pkrause at soundbite dot com)
 * @author Andrew C. Oliver (acoliver at apache dot org)
 * @author Jason Height (jheight at chariot dot net dot au)
 */

public class BoolPtg
    extends Ptg
{
    public final static int  SIZE = 2;
    public final static byte sid  = 0x1d;
    private boolean          field_1_value;

    private String val;

    private BoolPtg() {
    }

    public BoolPtg(byte [] data, int offset)
    {
        field_1_value = (data[offset + 1] == 1);
    }


    public BoolPtg(String formulaToken) {
        field_1_value = (formulaToken.equals("TRUE"));
    }

    public void setValue(boolean value)
    {
        field_1_value = value;
    }

    public boolean getValue()
    {
        return field_1_value;
    }

    public void writeBytes(byte [] array, int offset)
    {
        array[ offset + 0 ] = sid;
        array[ offset + 1 ] = (byte) (field_1_value ? 1 : 0);
    }

    public int getSize()
    {
        return SIZE;
    }

    public String toFormulaString(Workbook book)
    {
        return field_1_value ? "TRUE" : "FALSE";
    }

    public byte getDefaultOperandClass() {return Ptg.CLASS_VALUE;}

    public Object clone() {
        BoolPtg ptg = new BoolPtg();
        ptg.field_1_value = field_1_value;
        return ptg;
    }
}
