package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.BitField;

/**
 * Title: ColumnInfo Record<P>
 * Description:  Defines with width and formatting for a range of columns<P>
 * REFERENCE:  PG 293 Microsoft Excel 97 Developer's Kit (ISBN: 1-57231-498-2)<P>
 * @author Andrew C. Oliver (acoliver at apache dot org)
 * @version 2.0-pre
 */

public class ColumnInfoRecord
    extends Record
{
    public static final short     sid = 0x7d;
    private short                 field_1_first_col;
    private short                 field_2_last_col;
    private short                 field_3_col_width;
    private short                 field_4_xf_index;
    private short                 field_5_options;
    static final private BitField hidden    = new BitField(0x01);
    static final private BitField outlevel  = new BitField(0x0700);
    static final private BitField collapsed = new BitField(0x1000);
    private short                 field_6_reserved;

    public ColumnInfoRecord()
    {
    }

    /**
     * Constructs a ColumnInfo record and sets its fields appropriately
     *
     * @param id     id must be 0x7d or an exception will be throw upon validation
     * @param size  the size of the data area of the record
     * @param data  data of the record (should not contain sid/len)
     */

    public ColumnInfoRecord(short id, short size, byte [] data)
    {
        super(id, size, data);
    }

    /**
     * Constructs a ColumnInfo record and sets its fields appropriately
     *
     * @param id     id must be 0x7d or an exception will be throw upon validation
     * @param size  the size of the data area of the record
     * @param data  data of the record (should not contain sid/len)
     * @param offset of the record's data
     */

    public ColumnInfoRecord(short id, short size, byte [] data, int offset)
    {
        super(id, size, data);
    }

    protected void fillFields(byte [] data, short size, int offset)
    {
        field_1_first_col = LittleEndian.getShort(data, 0 + offset);
        field_2_last_col  = LittleEndian.getShort(data, 2 + offset);
        field_3_col_width = LittleEndian.getShort(data, 4 + offset);
        field_4_xf_index  = LittleEndian.getShort(data, 6 + offset);
        field_5_options   = LittleEndian.getShort(data, 8 + offset);
        field_6_reserved  = data[ 10 + offset ];
    }

    protected void validateSid(short id)
    {
        if (id != sid)
        {
            throw new RecordFormatException("NOT A COLINFO RECORD!!");
        }
    }

    /**
     * set the first column this record defines formatting info for
     * @param fc - the first column index (0-based)
     */

    public void setFirstColumn(short fc)
    {
        field_1_first_col = fc;
    }

    /**
     * set the last column this record defines formatting info for
     * @param lc - the last column index (0-based)
     */

    public void setLastColumn(short lc)
    {
        field_2_last_col = lc;
    }

    /**
     * set the columns' width in 1/256 of a character width
     * @param cw - column width
     */

    public void setColumnWidth(short cw)
    {
        field_3_col_width = cw;
    }

    /**
     * set the columns' default format info
     * @param xfi - the extended format index
     * @see org.apache.poi.hssf.record.ExtendedFormatRecord
     */

    public void setXFIndex(short xfi)
    {
        field_4_xf_index = xfi;
    }

    /**
     * set the options bitfield - use the bitsetters instead
     * @param options - the bitfield raw value
     */

    public void setOptions(short options)
    {
        field_5_options = options;
    }


    /**
     * set whether or not these cells are hidden
     * @param ishidden - whether the cells are hidden.
     * @see #setOptions(short)
     */

    public void setHidden(boolean ishidden)
    {
        field_5_options = hidden.setShortBoolean(field_5_options, ishidden);
    }

    /**
     * set the outline level for the cells
     * @see #setOptions(short)
     * @param olevel -outline level for the cells
     */

    public void setOutlineLevel(short olevel)
    {
        field_5_options = outlevel.setShortValue(field_5_options, olevel);
    }

    /**
     * set whether the cells are collapsed
     * @param iscollapsed - wether the cells are collapsed
     * @see #setOptions(short)
     */

    public void setCollapsed(boolean iscollapsed)
    {
        field_5_options = collapsed.setShortBoolean(field_5_options,
                                                    iscollapsed);
    }


    /**
     * get the first column this record defines formatting info for
     * @return the first column index (0-based)
     */

    public short getFirstColumn()
    {
        return field_1_first_col;
    }

    /**
     * get the last column this record defines formatting info for
     * @return the last column index (0-based)
     */

    public short getLastColumn()
    {
        return field_2_last_col;
    }

    /**
     * get the columns' width in 1/256 of a character width
     * @return column width
     */

    public short getColumnWidth()
    {
        return field_3_col_width;
    }

    /**
     * get the columns' default format info
     * @return the extended format index
     * @see org.apache.poi.hssf.record.ExtendedFormatRecord
     */

    public short getXFIndex()
    {
        return field_4_xf_index;
    }

    /**
     * get the options bitfield - use the bitsetters instead
     * @return the bitfield raw value
     */

    public short getOptions()
    {
        return field_5_options;
    }


    /**
     * get whether or not these cells are hidden
     * @return whether the cells are hidden.
     * @see #setOptions(short)
     */

    public boolean getHidden()
    {
        return hidden.isSet(field_5_options);
    }

    /**
     * get the outline level for the cells
     * @see #setOptions(short)
     * @return outline level for the cells
     */

    public short getOutlineLevel()
    {
        return outlevel.getShortValue(field_5_options);
    }

    /**
     * get whether the cells are collapsed
     * @return wether the cells are collapsed
     * @see #setOptions(short)
     */

    public boolean getCollapsed()
    {
        return collapsed.isSet(field_5_options);
    }

    public short getSid()
    {
        return sid;
    }

    public int serialize(int offset, byte [] data)
    {
        LittleEndian.putShort(data, 0 + offset, sid);
        LittleEndian.putShort(data, 2 + offset, ( short ) 12);
        LittleEndian.putShort(data, 4 + offset, getFirstColumn());
        LittleEndian.putShort(data, 6 + offset, getLastColumn());
        LittleEndian.putShort(data, 8 + offset, getColumnWidth());
        LittleEndian.putShort(data, 10 + offset, getXFIndex());
        LittleEndian.putShort(data, 12 + offset, getOptions());
        LittleEndian.putShort(data, 14 + offset,
        return getRecordSize();
    }

    public int getRecordSize()
    {
        return 16;
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("[COLINFO]\n");
        buffer.append("colfirst       = ").append(getFirstColumn())
            .append("\n");
        buffer.append("collast        = ").append(getLastColumn())
            .append("\n");
        buffer.append("colwidth       = ").append(getColumnWidth())
            .append("\n");
        buffer.append("xfindex        = ").append(getXFIndex()).append("\n");
        buffer.append("options        = ").append(getOptions()).append("\n");
        buffer.append("  hidden       = ").append(getHidden()).append("\n");
        buffer.append("  olevel       = ").append(getOutlineLevel())
            .append("\n");
        buffer.append("  collapsed    = ").append(getCollapsed())
            .append("\n");
        buffer.append("[/COLINFO]\n");
        return buffer.toString();
    }

    public Object clone() {
        ColumnInfoRecord rec = new ColumnInfoRecord();
        rec.field_1_first_col = field_1_first_col;
        rec.field_2_last_col = field_2_last_col;
        rec.field_3_col_width = field_3_col_width;
        rec.field_4_xf_index = field_4_xf_index;
        rec.field_5_options = field_5_options;
        rec.field_6_reserved = field_6_reserved;
        return rec;
    }
}
