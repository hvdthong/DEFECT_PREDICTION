package org.apache.poi.hssf.record.formula;

import java.util.List;

import org.apache.poi.hssf.model.Workbook;

/**
 * Unary Plus operator
 * does not have any effect on the operand
 * @author Avik Sengupta
 */

public class UnaryPlusPtg extends OperationPtg
{
    public final static int  SIZE = 1;
    public final static byte sid  = 0x12;
    
    private final static String ADD = "+";

    /** Creates new AddPtg */

    public UnaryPlusPtg()
    {
    }

    public UnaryPlusPtg(byte[] data, int offset)
    {

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
        return this.TYPE_UNARY;
    }

    public int getNumberOfOperands()
    {
        return 1;
    }
    
    /** Implementation of method from Ptg */
    public String toFormulaString(Workbook book)
    {
        return "+";
    }
       
   /** implementation of method from OperationsPtg*/  
    public String toFormulaString(String[] operands) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(ADD);
        buffer.append(operands[ 0]);
        return buffer.toString();
    }
    
    public byte getDefaultOperandClass() {return Ptg.CLASS_VALUE;}
           
    public Object clone() {
      return new UnaryPlusPtg();
    }

}
