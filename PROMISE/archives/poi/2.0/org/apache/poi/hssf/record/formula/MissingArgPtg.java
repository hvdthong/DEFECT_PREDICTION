package org.apache.poi.hssf.record.formula;

import org.apache.poi.hssf.model.Workbook;

/**
 * Missing Function Arguments
 *
 * Avik Sengupta <avik at apache.org>
 * @author Jason Height (jheight at chariot dot net dot au)
 */
public class MissingArgPtg
    extends  Ptg
{
   
    private final static int SIZE = 1;
    public final static byte sid  = 0x16;
   
    public MissingArgPtg()
    {
    }

    public MissingArgPtg(byte [] data, int offset)
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

   
    public String toFormulaString(Workbook book)
    {
        return " ";
    }
    
    public byte getDefaultOperandClass() {return Ptg.CLASS_VALUE;}
        
    public Object clone() {
      return new MissingArgPtg();
    }

}
