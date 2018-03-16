package org.apache.poi.hssf.record;

/**
 * The cell value record interface is implemented by all classes of type Record that
 * contain cell values.  It allows the containing sheet to move through them and compare
 * them.
 *
 * @author Andrew C. Oliver (acoliver at apache dot org)
 * @author Jason Height (jheight at chariot dot net dot au)
 *
 * @see org.apache.poi.hssf.model.Sheet
 * @see org.apache.poi.hssf.record.Record
 * @see org.apache.poi.hssf.record.RecordFactory
 */

public interface CellValueRecordInterface
{

    /**
     * get the row this cell occurs on
     *
     * @return the row
     */

    public int getRow();

    /**
     * get the column this cell defines within the row
     *
     * @return the column
     */

    public short getColumn();

    /**
     * set the row this cell occurs on
     * @param row the row this cell occurs within
     */

    public void setRow(int row);

    /**
     * set the column this cell defines within the row
     *
     * @param col the column this cell defines
     */

    public void setColumn(short col);

    public void setXFIndex(short xf);

    public short getXFIndex();

    /**
     * returns whether this cell is before the passed in cell
     *
     * @param i  another cell interface record to compare
     * @return true if the cells is before, or false if not
     */

    public boolean isBefore(CellValueRecordInterface i);

    /**
     * returns whether this cell is after the passed in cell
     *
     * @param i  record to compare
     * @return true if the cell is after, false if not
     */

    public boolean isAfter(CellValueRecordInterface i);

    /**
     * returns whether this cell represents the same cell (NOT VALUE)
     *
     * @param i  record to compare
     * @return true if the cells are the same cell (positionally), false if not.
     */

    public boolean isEqual(CellValueRecordInterface i);

    public Object clone();
}
