package org.apache.poi.hssf.record.formula;

import java.util.List;

import org.apache.poi.hssf.model.Workbook;

/**
 * Unary Plus operator
 * does not have any effect on the operand
 * @author Avik Sengupta
 */

public class UnaryMinusPtg extends OperationPtg
{
    public final static int  SIZE = 1;
    public final static byte sid  = 0x13;
    
    private final static String MINUS = "-";

    /** Creates new AddPtg */

    public UnaryMinusPtg()
    {
    }

    public UnaryMinusPtg(byte[] data, int offset)
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
        buffer.append(MINUS);
        buffer.append(operands[ 0]);
        return buffer.toString();
    }
    
    public byte getDefaultOperandClass() {return Ptg.CLASS_VALUE;}
           
    public Object clone() {
      return new UnaryPlusPtg();
    }

}
