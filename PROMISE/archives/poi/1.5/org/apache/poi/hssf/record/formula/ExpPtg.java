package org.apache.poi.hssf.record.formula;

/**
 *
 * @author  andy
 */

public class ExpPtg
    extends Ptg
{
    private final static int  SIZE = 5;
    public final static short sid  = 0x1;

    /** Creates new ExpPtg */

    public ExpPtg()
    {
    }

    /** Creates new ExpPtg */

    public ExpPtg(byte [] array, int offset)
    {
    }

    public void writeBytes(byte [] array, int offset)
    {
    }

    public int getSize()
    {
        return SIZE;
    }

    public String toFormulaString()
    {
        return "NO IDEA SHARED FORMULA EXP PTG";
    }
}
