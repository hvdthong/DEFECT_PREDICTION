package org.apache.poi.hssf.record.formula;

import org.apache.poi.util.BitField;
import org.apache.poi.util.LittleEndian;

/**
 * An excel function with variable number of value arguments.
 * @author  andy
 */

public class ValueVariableFunctionPtg
    extends Ptg
    implements OperationPtg
{
    public final static short  sid  = 0x42;
    private final static short SIZE = 4;
    private byte               field_1_byte_1;
    BitField                   numArgs = new BitField(0x7f);
    BitField                   prompt  = new BitField(0x80);
    private short              field_2_bytes;
    BitField                   functionIndex = new BitField(0x7FFF);
    BitField                   command       = new BitField(0x8000);

    /** Creates new ValueVariableFunctionPtg */

    public ValueVariableFunctionPtg()
    {
    }

    public ValueVariableFunctionPtg(byte [] data, int offset)
    {
        offset++;
        field_1_byte_1 = data[ offset + 0 ];
        field_2_bytes  = LittleEndian.getShort(data, offset + 1);
    }

    public void writeBytes(byte [] array, int offset)
    {
    }

    public int getSize()
    {
        return SIZE;
    }

    public short getNumArgs()
    {
        return numArgs.getShortValue(field_1_byte_1);
    }

    public int getNumberOfOperands()
    {
        return getNumArgs();
    }

    public int getType()
    {
        return -1;
    }

    public String toFormulaString()
    {
        return "NO IDEA YET VALUE VARIABLE";
    }

    public String toFormulaString(Ptg [] operands)
    {
        return toFormulaString();
    }
}
