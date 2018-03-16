package org.apache.poi.hssf.record.formula;

/**
 *
 * @author  andy
 */

public class AddPtg
    extends Ptg
    implements OperationPtg
{
    public final static int  SIZE = 1;
    public final static byte sid  = 0x03;
    
    private final static String ADD = "+";

    /** Creates new AddPtg */

    public AddPtg()
    {
    }

    public AddPtg(byte [] data, int offset)
    {

    }
    
    protected AddPtg(String formula, int offset) {
        
    }

    public void writeBytes(byte [] array, int offset)
    {
        array[ offset + 0 ] = sid;
    }

    public int getSize()
    {
        return SIZE;
    }

    public int getType()
    {
        return TYPE_BINARY;
    }

    public int getNumberOfOperands()
    {
        return 2;
    }

    public String toFormulaString()
    {
        return "+";
    }
    
    
    public static boolean isNextStringToken(String formula, int pos) {
        boolean retval = false;
        while (pos < formula.length() && Character.isWhitespace(formula.charAt(pos))) {
            pos++;
        }
        
        if (pos < formula.length()) {
            if (formula.charAt(pos) == ADD.toCharArray()[0]) {
                retval = true;
            }
        }
        return retval;
    }

    public String toFormulaString(Ptg [] operands)
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append(operands[ 0 ].toFormulaString());
        buffer.append("+");
        buffer.append(operands[ 1 ].toFormulaString());
        return buffer.toString();
    }
    
    public int getPrecedence() {
        return 5;
    }
    
    public int getStringLength() {
        return 1;
    }
}
