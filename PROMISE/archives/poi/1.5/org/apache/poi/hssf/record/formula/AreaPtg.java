package org.apache.poi.hssf.record.formula;

import org.apache.poi.util.LittleEndian;

/**
 *
 * @author  andy
 */

public class AreaPtg
    extends Ptg
{
    public final static short sid  = 0x25;
    private final static int  SIZE = 9;
    private short             field_1_first_row;
    private short             field_2_last_row;
    private short             field_3_first_column;
    private short             field_4_last_column;

    /** Creates new AreaPtg */

    public AreaPtg()
    {
    }

    public AreaPtg(byte [] data, int offset)
    {
        offset++;
        field_1_first_row    = LittleEndian.getShort(data, 0 + offset);
        field_2_last_row     = LittleEndian.getShort(data, 2 + offset);
        field_3_first_column = LittleEndian.getShort(data, 4 + offset);
        field_4_last_column  = LittleEndian.getShort(data, 6 + offset);
        System.out.println(toString());
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("AreaPtg\n");
        buffer.append("firstRow = " + getFirstRow()).append("\n");
        buffer.append("lastRow  = " + getLastRow()).append("\n");
        buffer.append("firstCol = " + getFirstColumn()).append("\n");
        buffer.append("lastCol  = " + getLastColumn()).append("\n");
        buffer.append("firstColRowRel= "
                      + isFirstColRowRelative()).append("\n");
        buffer.append("lastColRowRel = "
                      + isLastColRowRelative()).append("\n");
        buffer.append("firstColRel   = " + isFirstColRelative()).append("\n");
        buffer.append("lastColRel    = " + isLastColRelative()).append("\n");
        return buffer.toString();
    }

    public void writeBytes(byte [] array, int offset)
    {
    }

    public int getSize()
    {
        return SIZE;
    }

    public short getFirstRow()
    {
        return field_1_first_row;
    }

    public void setFirstRow(short row)
    {
        field_1_first_row = row;
    }

    public short getLastRow()
    {
        return field_2_last_row;
    }

    public void setLastRow(short row)
    {
        field_2_last_row = row;
    }

    public short getFirstColumn()
    {
        return ( short ) (field_3_first_column & 0x3FFF);
    }

    public short getFirstColumnRaw()
    {
        return field_3_first_column;
    }

    public boolean isFirstColRowRelative()
    {
        return (((getFirstColumnRaw()) & 0x8000) == 0x8000);
    }

    public boolean isFirstColRelative()
    {
        return (((getFirstColumnRaw()) & 0x4000) == 0x4000);
    }

    public void setFirstColumn(short column)
    {
    }

    public void setFirstColumnRaw(short column)
    {
        field_3_first_column = column;
    }

    public short getLastColumn()
    {
    }

    public short getLastColumnRaw()
    {
        return field_4_last_column;
    }

    public boolean isLastColRowRelative()
    {
        return (((getLastColumnRaw()) & 0x8000) == 1);
    }

    public boolean isLastColRelative()
    {
        return (((getFirstColumnRaw()) & 0x4000) == 1);
    }

    public void setLastColumn(short column)
    {
    }

    public void setLastColumnRaw(short column)
    {
        field_4_last_column = column;
    }

    public String toFormulaString()
    {
        String firstrow = "" + (getFirstRow() + 1);
        String lastrow  = null;

        if (isLastColRowRelative())
        {
            lastrow = "" + (getFirstRow() + getLastRow());
        }
        else
        {
            lastrow = "" + (getLastRow() + 1);
        }

        return colNumToLetter(getFirstColumn()) + firstrow + ":"
               + colNumToLetter(getLastColumn()) + lastrow;
    }

    public String colNumToLetter(int col)
    {
        byte[] b =
        {
            0x41
        };

        b[ 0 ] += ( byte ) col;
        String retval = null;

        try
        {
            retval = new String(b, "UTF-8");
        }
        catch (java.io.UnsupportedEncodingException e)
        {
            throw new RuntimeException(
                "NON JDK 1.3 COMPLIANT JVM -- YOUR JVM MUST SUPPORT UTF-8 encoding as per docs!");
        }
        return retval;
    }
}
