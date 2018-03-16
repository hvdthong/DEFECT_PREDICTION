package org.apache.poi.hssf.record.formula;

/**
 *
 * @author  andy
 */

public class PowerPtg
    extends Ptg
    implements OperationPtg
{
    public final static int  SIZE = 1;
    public final static byte sid  = 0x07;

    /** Creates new AddPtg */

    public PowerPtg()
    {
    }

    public PowerPtg(byte [] data, int offset)
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

    public String toFormulaString()
    {
        return "^";
    }

    public String toFormulaString(Ptg [] operands)
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append(operands[ 1 ].toFormulaString());
        buffer.append("^");
        buffer.append(operands[ 0 ].toFormulaString());
        return buffer.toString();
    }
}
