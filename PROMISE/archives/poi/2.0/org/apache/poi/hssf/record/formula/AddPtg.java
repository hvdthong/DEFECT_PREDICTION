package org.apache.poi.hssf.record.formula;

import java.util.List;

import org.apache.poi.hssf.model.Workbook;

/**
 * Addition operator PTG the "+" binomial operator.  If you need more 
 * explanation than that then well...We really can't help you here.
 * @author  Andrew C. Oliver (acoliver@apache.org)
 * @author Jason Height (jheight at chariot dot net dot au)
 */

public class AddPtg
    extends OperationPtg
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
    
    /** Implementation of method from Ptg */
    public String toFormulaString(Workbook book)
    {
        return "+";
    }
       
   /** implementation of method from OperationsPtg*/  
    public String toFormulaString(String[] operands) {
        StringBuffer buffer = new StringBuffer();

        buffer.append(operands[ 0 ]);
        buffer.append(ADD);
        buffer.append(operands[ 1 ]);
        return buffer.toString();
    }
    
    public byte getDefaultOperandClass() {return Ptg.CLASS_VALUE;}
           
    public Object clone() {
      return new AddPtg();
    }

}
