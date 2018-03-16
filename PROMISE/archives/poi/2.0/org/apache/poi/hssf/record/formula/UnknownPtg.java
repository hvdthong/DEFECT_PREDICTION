package org.apache.poi.hssf.record.formula;

import org.apache.poi.hssf.model.Workbook;

/**
 *
 * @author  andy
 * @author Jason Height (jheight at chariot dot net dot au)
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

    public String toFormulaString(Workbook book)
    {
        return "UNKNOWN";
    }
    public byte getDefaultOperandClass() {return Ptg.CLASS_VALUE;}

    public Object clone() {
      return new UnknownPtg();
    }

    
}
