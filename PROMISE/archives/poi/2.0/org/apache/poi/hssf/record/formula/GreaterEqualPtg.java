package org.apache.poi.hssf.record.formula;


import org.apache.poi.hssf.model.Workbook;

/**
 * PTG class to implement greater or equal to
 * @author  fred at stsci dot edu
 */
public class GreaterEqualPtg
    extends OperationPtg
{
    public final static int  SIZE = 1;
    public final static byte sid  = 0x0c;

    /** Creates new GreaterEqualPtg */

   public GreaterEqualPtg()
    {
    }

    public GreaterEqualPtg(byte [] data, int offset)
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

    public String toFormulaString(Workbook book)
    {
        return ">=";
    }
 
    public String toFormulaString(String[] operands) {
         StringBuffer buffer = new StringBuffer();

        
        buffer.append(operands[ 0 ]);
        buffer.append(toFormulaString((Workbook)null));
        buffer.append(operands[ 1 ]);
        return buffer.toString();
    }       

    public Object clone() {
      return new GreaterEqualPtg();
    }


}
