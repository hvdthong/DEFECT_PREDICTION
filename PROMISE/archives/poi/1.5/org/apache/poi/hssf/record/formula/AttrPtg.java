package org.apache.poi.hssf.record.formula;

import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.BitField;

/**
 * "Special Attributes"
 * This seems to be a Misc Stuff and Junk record.  One function it serves is
 * in SUM functions (i.e. SUM(A1:A3) causes an area PTG then an ATTR with the SUM option set)
 * @author  andy
 */

public class AttrPtg
    extends Ptg
    implements OperationPtg
{
    public final static short sid  = 0x19;
    private final static int  SIZE = 4;
    private byte              field_1_options;
    private short             field_2_data;
    private BitField          semiVolatile = new BitField(0x01);
    private BitField          optiIf       = new BitField(0x02);
    private BitField          optiChoose   = new BitField(0x04);
    private BitField          optGoto      = new BitField(0x08);
    private BitField          sum          = new BitField(0x10);
    private BitField          baxcel       = new BitField(0x20);
    private BitField          space        = new BitField(0x40);

    /** Creates new AttrPtg */

    public AttrPtg()
    {
    }

    public AttrPtg(byte [] data, int offset)
    {
        field_1_options = data[ offset + 0 ];
        field_2_data    = LittleEndian.getShort(data, offset + 1);
        System.out.println("OPTIONS = " + Integer.toHexString(getOptions()));
        System.out.println("OPTIONS & 0x10 = " + (getOptions() & 0x10));
        System.out.println(toString());
    }

    public void setOptions(byte options)
    {
        field_1_options = options;
    }

    public byte getOptions()
    {
        return field_1_options;
    }

    public boolean isSemiVolatile()
    {
        return semiVolatile.isSet(getOptions());
    }

    public boolean isOptimizedIf()
    {
        return optiIf.isSet(getOptions());
    }

    public boolean isOptimizedChoose()
    {
        return optiChoose.isSet(getOptions());
    }

    public boolean isGoto()
    {
        return optGoto.isSet(getOptions());
    }

    public boolean isSum()
    {
        return sum.isSet(getOptions());
    }

    public boolean isBaxcel()
    {
        return baxcel.isSet(getOptions());
    }

    public boolean isSpace()
    {
        return space.isSet(getOptions());
    }

    public void setData(short data)
    {
        field_2_data = data;
    }

    public short getData()
    {
        return field_2_data;
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("AttrPtg\n");
        buffer.append("options=").append(field_1_options).append("\n");
        buffer.append("data   =").append(field_2_data).append("\n");
        buffer.append("semi   =").append(isSemiVolatile()).append("\n");
        buffer.append("optimif=").append(isOptimizedIf()).append("\n");
        buffer.append("optchos=").append(isOptimizedChoose()).append("\n");
        buffer.append("isGoto =").append(isGoto()).append("\n");
        buffer.append("isSum  =").append(isSum()).append("\n");
        buffer.append("isBaxce=").append(isBaxcel()).append("\n");
        buffer.append("isSpace=").append(isSpace()).append("\n");
        return buffer.toString();
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
        return "SUM()";
    }

    public String toFormulaString(Ptg [] operands)
    {
        return "SUM(" + operands[ 0 ].toFormulaString() + ")";
    }

    public int getNumberOfOperands()
    {
        return 1;
    }

    public int getType()
    {
        return -1;
    }
}
