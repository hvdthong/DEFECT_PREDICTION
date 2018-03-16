package org.apache.poi.hssf.record.formula;

import org.apache.poi.util.LittleEndian;

/**
 * Integer (short intger)
 * Stores a (java) short value in a formula
 * @author  andy
 */

public class IntPtg
    extends Ptg
{
    public final static int  SIZE = 3;
    public final static byte sid  = 0x1e;
    private short            field_1_value;

    private String val;
    private int strlen = 0;
    /** Creates new IntPtg */

    public IntPtg()
    {
    }

    public IntPtg(byte [] data, int offset)
    {
        setValue(LittleEndian.getShort(data, offset + 1));
    }
    
    protected IntPtg(String formula, int offset) {
        val = parseString(formula, offset);
        if (val == null) throw new RuntimeException("WHOOAA there...thats got no int!");
        strlen=val.length();
        field_1_value = Short.parseShort(val);
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

    public String toFormulaString()
    {
        return "" + getValue();
    }
    
    private static String parseString(String formula, int pos) {
        String retval = null;
        while (pos < formula.length() && Character.isWhitespace(formula.charAt(pos))) {
            pos++;
        }
        
        if (pos < formula.length()) {
            if (Character.isDigit(formula.charAt(pos)) ) {
                int numpos = pos;
                
                while (numpos < formula.length() && Character.isDigit(formula.charAt(numpos))){
                    numpos++;
                }
                
                if (numpos == formula.length() || formula.charAt(numpos) != '.') {
                    String numberstr = formula.substring(pos,numpos);
                    try {
                        int number = Short.parseShort(numberstr);
                        retval = numberstr;
                    } catch (NumberFormatException e) {
                        retval = null;
                    }
                }
            }
        }
        return retval;
        
    }
    
    public static boolean isNextStringToken(String formula, int pos) {
        return (parseString(formula,pos) != null);
    }
    
    public int getPrecedence() {
        return 5;
    }
    
    public int getStringLength() {
        return strlen;
    }    
}
