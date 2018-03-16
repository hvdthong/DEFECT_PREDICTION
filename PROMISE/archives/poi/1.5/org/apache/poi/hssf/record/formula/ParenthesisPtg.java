package org.apache.poi.hssf.record.formula;

/**
 * Dummy class, we want it only for for the parsing process
 * does not actually get into the file  -- note by andy...there is a parenthesis PTG
 * that can be written and is sometimes!
 *
 * Avik Sengupta <lists@aviksengupta.com>
 */
public class ParenthesisPtg
    extends Ptg
    implements OperationPtg
{
   

   

    public void writeBytes(byte [] array, int offset)
    {
    }

    public int getSize()
    {
        return 0;
    }

    public int getType()
    {
        return TYPE_BINARY;
    }

    public int getNumberOfOperands()
    {
        return 0;
    }

    public String toFormulaString()
    {
        return "(";
    }

    public String toFormulaString(Ptg [] operands)
    {
        return "(";
    }
}

