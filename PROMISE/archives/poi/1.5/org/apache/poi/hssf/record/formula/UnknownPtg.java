package org.apache.poi.hssf.record.formula;

/**
 *
 * @author  andy
 */

public class UnknownPtg
    extends Ptg
{
    private short size;

    /** Creates new UnknownPtg */

    public UnknownPtg()
    {
    }

    public UnknownPtg(byte [] data, int offset)
    {

    }

    public void writeBytes(byte [] array, int offset)
    {
    }

    public int getSize()
    {
        return size;
    }

    public String toFormulaString()
    {
        return "UNKNOWN";
    }
}
