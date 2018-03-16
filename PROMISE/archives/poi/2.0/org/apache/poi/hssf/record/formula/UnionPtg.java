package org.apache.poi.hssf.record.formula;

import org.apache.poi.hssf.model.Workbook;

/**
 * @author Glen Stampoultzis (glens at apache.org)
 */
public class UnionPtg extends OperationPtg
{
    public final static byte sid  = 0x10;


    public UnionPtg()
    {
    }

    public UnionPtg(byte [] data, int offset)
    {
    }


    public int getSize()
    {
        return 1;
    }

    public void writeBytes( byte[] array, int offset )
    {
        array[ offset + 0 ] = sid;
    }

    public Object clone()
    {
        return new UnionPtg();
    }

    public int getType()
    {
        return TYPE_BINARY;
    }

    /** Implementation of method from Ptg */
    public String toFormulaString(Workbook book)
    {
        return ",";
    }


    /** implementation of method from OperationsPtg*/
    public String toFormulaString(String[] operands)
    {
         StringBuffer buffer = new StringBuffer();

         buffer.append(operands[ 0 ]);
         buffer.append(",");
         buffer.append(operands[ 1 ]);
         return buffer.toString();
     }

    public int getNumberOfOperands()
    {
        return 2;
    }

}
