package org.apache.poi.hssf.record;

import java.util.Stack;
import java.util.List;

import org.apache.poi.util.LittleEndian;
import org.apache.poi.hssf.record.formula.*;

/**
 * Formula Record - This is not really supported in this release.  Its here for future use.<P>
 * REFERENCE:  PG 317/444 Microsoft Excel 97 Developer's Kit (ISBN: 1-57231-498-2)<P>
 * @author Andrew C. Oliver (acoliver at apache dot org)
 * @version 2.0-pre
 */

public class FormulaRecord
    extends Record
    implements CellValueRecordInterface, Comparable
{
    
    public static final boolean EXPERIMENTAL_FORMULA_SUPPORT_ENABLED=false;
    
    public static final short sid =
    
    private short             field_1_row;
    private short             field_2_column;
    private short             field_3_xf;
    private double            field_4_value;
    private short             field_5_options;
    private int               field_6_zero;
    private short             field_7_expression_len;
    private Stack             field_8_parsed_expr;
    

    /** Creates new FormulaRecord */

    public FormulaRecord()
    {
        field_8_parsed_expr = new Stack();
    }

    /**
     * Constructs a Formula record and sets its fields appropriately.
     *
     * @param id     id must be 0x06 (NOT 0x406 see MSKB #Q184647 for an "explanation of
     * this bug in the documentation) or an exception will be throw upon validation
     * @param size  the size of the data area of the record
     * @param data  data of the record (should not contain sid/len)
     */

    public FormulaRecord(short id, short size, byte [] data)
    {
        super(id, size, data);
    }

    /**
     * Constructs a Formula record and sets its fields appropriately.
     *
     * @param id     id must be 0x06 (NOT 0x406 see MSKB #Q184647 for an "explanation of
     * this bug in the documentation) or an exception will be throw upon validation
     * @param size  the size of the data area of the record
     * @param data  data of the record (should not contain sid/len)
     * @param offset of the record's data
     */

    public FormulaRecord(short id, short size, byte [] data, int offset)
    {
        super(id, size, data, offset);
    }

    protected void fillFields(byte [] data, short size, int offset)
    {
        if (EXPERIMENTAL_FORMULA_SUPPORT_ENABLED) {
        field_1_row            = LittleEndian.getShort(data, 0 + offset);
        field_2_column         = LittleEndian.getShort(data, 2 + offset);
        field_3_xf             = LittleEndian.getShort(data, 4 + offset);
        field_4_value          = LittleEndian.getDouble(data, 6 + offset);
        field_5_options        = LittleEndian.getShort(data, 14 + offset);
        field_6_zero           = LittleEndian.getInt(data, 16 + offset);
        field_7_expression_len = LittleEndian.getShort(data, 20 + offset);
        field_8_parsed_expr    = getParsedExpressionTokens(data, size,
                                 offset);
        
        } else {
            all_data = new byte[size+4];
            LittleEndian.putShort(all_data,0,sid);
            LittleEndian.putShort(all_data,2,size);
            System.arraycopy(data,offset,all_data,4,size);
        }

    }

    private Stack getParsedExpressionTokens(byte [] data, short size,
                                            int offset)
    {
        Stack stack = new Stack();
        /*int   pos   = 22 + offset;

        while (pos < size)
        {
            Ptg ptg = Ptg.createPtg(data, pos);

            pos += ptg.getSize();
            stack.push(ptg);
        }*/
        return stack;
    }

    public void setRow(short row)
    {
        field_1_row = row;
    }

    public void setColumn(short column)
    {
        field_2_column = column;
    }

    public void setXFIndex(short xf)
    {
        field_3_xf = xf;
    }

    /**
     * set the calculated value of the formula
     *
     * @param value  calculated value
     */

    public void setValue(double value)
    {
        field_4_value = value;
    }

    /**
     * set the option flags
     *
     * @param options  bitmask
     */

    public void setOptions(short options)
    {
        field_5_options = options;
    }

    /**
     * set the length (in number of tokens) of the expression
     * @param len  length
     */

    public void setExpressionLength(short len)
    {
        field_7_expression_len = len;
    }

    public short getRow()
    {
        return field_1_row;
    }

    public short getColumn()
    {
        return field_2_column;
    }

    public short getXFIndex()
    {
        return field_3_xf;
    }

    /**
     * get the calculated value of the formula
     *
     * @return calculated value
     */

    public double getValue()
    {
        return field_4_value;
    }

    /**
     * get the option flags
     *
     * @return bitmask
     */

    public short getOptions()
    {
        return field_5_options;
    }

    /**
     * get the length (in number of tokens) of the expression
     * @return  expression length
     */

    public short getExpressionLength()
    {
        return field_7_expression_len;
    }

    /**
     * push a token onto the stack
     *
     * @param ptg  the token
     */

    public void pushExpressionToken(Ptg ptg)
    {
        field_8_parsed_expr.push(ptg);
    }

    /**
     * pop a token off of the stack
     *
     * @return Ptg - the token
     */

    public Ptg popExpressionToken()
    {
        return ( Ptg ) field_8_parsed_expr.pop();
    }

    /**
     * peek at the token on the top of stack
     *
     * @return Ptg - the token
     */

    public Ptg peekExpressionToken()
    {
        return ( Ptg ) field_8_parsed_expr.peek();
    }

    /**
     * get the size of the stack
     * @return size of the stack
     */

    public int getNumberOfExpressionTokens()
    {
        return field_8_parsed_expr.size();
    }

    /**
     * get the stack as a list
     *
     * @return list of tokens (casts stack to a list and returns it!)
     */

    public List getParsedExpression()
    {
        return ( List ) field_8_parsed_expr;
    }

    /**
     * called by constructor, should throw runtime exception in the event of a
     * record passed with a differing ID.
     *
     * @param id alleged id for this record
     */

    protected void validateSid(short id)
    {
        if (id != sid)
        {
            throw new RecordFormatException("NOT A FORMULA RECORD");
        }
    }

    public short getSid()
    {
        return sid;
    }

    /**
     * called by the class that is responsible for writing this sucker.
     * Subclasses should implement this so that their data is passed back in a
     * byte array.
     *
     * @return byte array containing instance data
     */

    public int serialize(int offset, byte [] data)
    {
        if (EXPERIMENTAL_FORMULA_SUPPORT_ENABLED) {
        int ptgSize = getTotalPtgSize();

        LittleEndian.putShort(data, 0 + offset, sid);
        LittleEndian.putShort(data, 2 + offset, ( short ) (24 + ptgSize));
        LittleEndian.putShort(data, 4 + offset, getRow());
        LittleEndian.putShort(data, 6 + offset, getColumn());
        LittleEndian.putShort(data, 8 + offset, getXFIndex());
        LittleEndian.putDouble(data, 10 + offset, getValue());
        LittleEndian.putShort(data, 18 + offset, getOptions());
        LittleEndian.putInt(data, 20 + offset, field_6_zero);
        LittleEndian.putShort(data, 24 + offset, getExpressionLength());
        } else {
            System.arraycopy(all_data,0,data,offset,all_data.length);
        }

        return getRecordSize();
    }

    public int getRecordSize()
    {
        int retval =0;
        
        if (EXPERIMENTAL_FORMULA_SUPPORT_ENABLED) {
            retval = getTotalPtgSize() + 28;
        } else {
            retval =all_data.length;
        }
        return retval;

    }

    private int getTotalPtgSize()
    {
        List list   = getParsedExpression();
        int  retval = 0;

        for (int k = 0; k < list.size(); k++)
        {
            Ptg ptg = ( Ptg ) list.get(k);

            retval += ptg.getSize();
        }
        return retval;
    }

    private void serializePtgs(byte [] data, int offset)
    {
        int pos = offset;

        for (int k = 0; k < field_8_parsed_expr.size(); k++)
        {
            Ptg ptg = ( Ptg ) field_8_parsed_expr.get(k);

            ptg.writeBytes(data, pos);
            pos += ptg.getSize();
        }
    }

    public boolean isBefore(CellValueRecordInterface i)
    {
        if (this.getRow() > i.getRow())
        {
            return false;
        }
        if ((this.getRow() == i.getRow())
                && (this.getColumn() > i.getColumn()))
        {
            return false;
        }
        if ((this.getRow() == i.getRow())
                && (this.getColumn() == i.getColumn()))
        {
            return false;
        }
        return true;
    }

    public boolean isAfter(CellValueRecordInterface i)
    {
        if (this.getRow() < i.getRow())
        {
            return false;
        }
        if ((this.getRow() == i.getRow())
                && (this.getColumn() < i.getColumn()))
        {
            return false;
        }
        if ((this.getRow() == i.getRow())
                && (this.getColumn() == i.getColumn()))
        {
            return false;
        }
        return true;
    }

    public boolean isEqual(CellValueRecordInterface i)
    {
        return ((this.getRow() == i.getRow())
                && (this.getColumn() == i.getColumn()));
    }

    public boolean isInValueSection()
    {
        return true;
    }

    public boolean isValue()
    {
        return true;
    }

    public int compareTo(Object obj)
    {
        CellValueRecordInterface loc = ( CellValueRecordInterface ) obj;

        if ((this.getRow() == loc.getRow())
                && (this.getColumn() == loc.getColumn()))
        {
            return 0;
        }
        if (this.getRow() < loc.getRow())
        {
            return -1;
        }
        if (this.getRow() > loc.getRow())
        {
            return 1;
        }
        if (this.getColumn() < loc.getColumn())
        {
            return -1;
        }
        if (this.getColumn() > loc.getColumn())
        {
            return 1;
        }
        return -1;
    }

    public boolean equals(Object obj)
    {
        if (!(obj instanceof CellValueRecordInterface))
        {
            return false;
        }
        CellValueRecordInterface loc = ( CellValueRecordInterface ) obj;

        if ((this.getRow() == loc.getRow())
                && (this.getColumn() == loc.getColumn()))
        {
            return true;
        }
        return false;
    }
}
