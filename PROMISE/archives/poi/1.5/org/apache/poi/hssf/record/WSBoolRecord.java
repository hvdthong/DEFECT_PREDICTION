package org.apache.poi.hssf.record;

import org.apache.poi.util.BitField;
import org.apache.poi.util.LittleEndian;

/**
 * Title:        WSBool Record.<p>
 * Description:  stores workbook settings  (aka its a big "everything we didn't
 *               put somewhere else")<P>
 * REFERENCE:  PG 425 Microsoft Excel 97 Developer's Kit (ISBN: 1-57231-498-2)<P>
 * @author Andrew C. Oliver (acoliver at apache dot org)
 * @author Glen Stampoultzis (gstamp@iprimus.com.au)
 * @version 2.0-pre
 */

public class WSBoolRecord
    extends Record
{
    public final static short     sid = 0x81;

    static final private BitField autobreaks          =

    static final private BitField dialog              =
    static final private BitField applystyles         =
    static final private BitField rowsumsbelow        = new BitField(
    static final private BitField rowsumsright        = new BitField(
    static final private BitField fittopage           =

    static final private BitField displayguts         = new BitField(

        new BitField(0x40);
        new BitField(0x80);

    public WSBoolRecord()
    {
    }

    /**
     * Constructs a WSBool record and sets its fields appropriately.
     *
     * @param id     id must be 0x81 or an exception will be throw upon validation
     * @param size  the size of the data area of the record
     * @param data  data of the record (should not contain sid/len)
     */

    public WSBoolRecord(short id, short size, byte [] data)
    {
        super(id, size, data);
    }

    /**
     * Constructs a WSBool record and sets its fields appropriately.
     *
     * @param id     id must be 0x81 or an exception will be throw upon validation
     * @param size  the size of the data area of the record
     * @param data  data of the record (should not contain sid/len)
     */

    public WSBoolRecord(short id, short size, byte [] data, int offset)
    {
        super(id, size, data, offset);
    }

    protected void validateSid(short id)
    {
        if (id != sid)
        {
            throw new RecordFormatException("NOT A WSBoolRECORD");
        }
    }

    protected void fillFields(byte [] data, short size, int offset)
    {
        field_1_wsbool =
        field_2_wsbool =


    /**
     * set first byte (see bit setters)
     */

    public void setWSBool1(byte bool1)
    {
        field_1_wsbool = bool1;
    }


    /**
     * show automatic page breaks or not
     * @param ab  whether to show auto page breaks
     */

    public void setAutobreaks(boolean ab)
    {
        field_1_wsbool = autobreaks.setByteBoolean(field_1_wsbool, ab);
    }

    /**
     * set whether sheet is a dialog sheet or not
     * @param isDialog or not
     */

    public void setDialog(boolean isDialog)
    {
        field_1_wsbool = dialog.setByteBoolean(field_1_wsbool, isDialog);
    }

    /**
     * set if row summaries appear below detail in the outline
     * @param below or not
     */

    public void setRowSumsBelow(boolean below)
    {
        field_1_wsbool = rowsumsbelow.setByteBoolean(field_1_wsbool, below);
    }

    /**
     * set if col summaries appear right of the detail in the outline
     * @param right or not
     */

    public void setRowSumsRight(boolean right)
    {
        field_1_wsbool = rowsumsright.setByteBoolean(field_1_wsbool, right);
    }


    /**
     * set the second byte (see bit setters)
     */

    public void setWSBool2(byte bool2)
    {
        field_2_wsbool = field_2_wsbool = bool2;
    }


    /**
     * fit to page option is on
     * @param fit2page  fit or not
     */

    public void setFitToPage(boolean fit2page)
    {
        field_2_wsbool = fittopage.setByteBoolean(field_2_wsbool, fit2page);
    }

    /**
     * set whether to display the guts or not
     *
     * @param guts or no guts (or glory)
     */

    public void setDisplayGuts(boolean guts)
    {
        field_2_wsbool = displayguts.setByteBoolean(field_2_wsbool, guts);
    }

    /**
     * whether alternate expression evaluation is on
     * @param altexp  alternative expression evaluation or not
     */

    public void setAlternateExpression(boolean altexp)
    {
        field_2_wsbool = alternateexpression.setByteBoolean(field_2_wsbool,
                altexp);
    }

    /**
     * whether alternative formula entry is on
     * @param formula  alternative formulas or not
     */

    public void setAlternateFormula(boolean formula)
    {
        field_2_wsbool = alternateformula.setByteBoolean(field_2_wsbool,
                formula);
    }


    /**
     * get first byte (see bit getters)
     */

    public byte getWSBool1()
    {
        return field_1_wsbool;
    }


    /**
     * show automatic page breaks or not
     * @return whether to show auto page breaks
     */

    public boolean getAutobreaks()
    {
        return autobreaks.isSet(field_1_wsbool);
    }

    /**
     * get whether sheet is a dialog sheet or not
     * @return isDialog or not
     */

    public boolean getDialog()
    {
        return dialog.isSet(field_1_wsbool);
    }

    /**
     * get if row summaries appear below detail in the outline
     * @return below or not
     */

    public boolean getRowSumsBelow()
    {
        return rowsumsbelow.isSet(field_1_wsbool);
    }

    /**
     * get if col summaries appear right of the detail in the outline
     * @return right or not
     */

    public boolean getRowSumsRight()
    {
        return rowsumsright.isSet(field_1_wsbool);
    }


    /**
     * get the second byte (see bit getters)
     */

    public byte getWSBool2()
    {
        return field_2_wsbool;
    }


    /**
     * fit to page option is on
     * @return fit or not
     */

    public boolean getFitToPage()
    {
        return fittopage.isSet(field_2_wsbool);
    }

    /**
     * get whether to display the guts or not
     *
     * @return guts or no guts (or glory)
     */

    public boolean getDisplayGuts()
    {
        return displayguts.isSet(field_2_wsbool);
    }

    /**
     * whether alternate expression evaluation is on
     * @return alternative expression evaluation or not
     */

    public boolean getAlternateExpression()
    {
        return alternateexpression.isSet(field_2_wsbool);
    }

    /**
     * whether alternative formula entry is on
     * @return alternative formulas or not
     */

    public boolean getAlternateFormula()
    {
        return alternateformula.isSet(field_2_wsbool);
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("[WSBOOL]\n");
        buffer.append("    .wsbool1        = ")
            .append(Integer.toHexString(getWSBool1())).append("\n");
        buffer.append("        .autobreaks = ").append(getAutobreaks())
            .append("\n");
        buffer.append("        .dialog     = ").append(getDialog())
            .append("\n");
        buffer.append("        .rowsumsbelw= ").append(getRowSumsBelow())
            .append("\n");
        buffer.append("        .rowsumsrigt= ").append(getRowSumsRight())
            .append("\n");
        buffer.append("    .wsbool2        = ")
            .append(Integer.toHexString(getWSBool2())).append("\n");
        buffer.append("        .fittopage  = ").append(getFitToPage())
            .append("\n");
        buffer.append("        .displayguts= ").append(getDisplayGuts())
            .append("\n");
        buffer.append("        .alternateex= ")
            .append(getAlternateExpression()).append("\n");
        buffer.append("        .alternatefo= ").append(getAlternateFormula())
            .append("\n");
        buffer.append("[/WSBOOL]\n");
        return buffer.toString();
    }

    public int serialize(int offset, byte [] data)
    {
        LittleEndian.putShort(data, 0 + offset, sid);
        LittleEndian.putShort(data, 2 + offset, ( short ) 0x2);
        data[ 5 + offset ] = getWSBool1();
        data[ 4 + offset ] = getWSBool2();
        return getRecordSize();
    }

    public int getRecordSize()
    {
        return 6;
    }

    public short getSid()
    {
        return this.sid;
    }
}
