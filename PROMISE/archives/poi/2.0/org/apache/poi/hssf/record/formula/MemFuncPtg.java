package org.apache.poi.hssf.record.formula;

import org.apache.poi.util.LittleEndian;
import org.apache.poi.hssf.model.Workbook;

/**
 * @author Glen Stampoultzis (glens at apache.org)
 */
public class MemFuncPtg extends ControlPtg
{

    public final static byte sid = 0x29;
    private short field_1_len_ref_subexpression = 0;

    public MemFuncPtg()
    {
    }

    /**Creates new function pointer from a byte array
     * usually called while reading an excel file.
     */
    public MemFuncPtg( byte[] data, int offset )
    {
        offset++;
        field_1_len_ref_subexpression = LittleEndian.getShort( data, offset + 0 );
    }

    public int getSize()
    {
        return 3;
    }

    public void writeBytes( byte[] array, int offset )
    {
        array[offset + 0] =  sid ;
        LittleEndian.putShort( array, offset + 1, (short)field_1_len_ref_subexpression );
    }

    public String toFormulaString(Workbook book)
    {
        return "";
    }

    public byte getDefaultOperandClass()
    {
        return 0;
    }

    public int getNumberOfOperands()
    {
        return field_1_len_ref_subexpression;
    }

    public Object clone()
    {
        MemFuncPtg ptg = new MemFuncPtg();
        ptg.field_1_len_ref_subexpression = this.field_1_len_ref_subexpression;
        return ptg;
    }

    public int getLenRefSubexpression()
    {
        return field_1_len_ref_subexpression;
    }

    public void setLenRefSubexpression(int len)
    {
        field_1_len_ref_subexpression = (short)len;
    }

}
