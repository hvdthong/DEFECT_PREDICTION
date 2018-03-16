package org.apache.poi.hssf.record.formula;

import org.apache.poi.util.LittleEndian;

/**
 *
 * @author  andy
 */

public class NamePtg
    extends Ptg
{
    public final static short sid  = 0x23;
    private final static int  SIZE = 7;
    private short             field_2_label_index;

    /** Creates new NamePtg */

    public NamePtg()
    {
    }

    /** Creates new NamePtg */

    public NamePtg(byte [] data, int offset)
    {
        offset++;
        field_1_ixti        = LittleEndian.getShort(data, offset);
        field_2_label_index = LittleEndian.getShort(data, offset + 2);
        field_3_zero        = LittleEndian.getShort(data, offset + 4);
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
        return "NO IDEA - NAME";
    }
}
