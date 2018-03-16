package org.apache.poi.hssf.record.aggregates;

import org.apache.poi.hssf.record.CellValueRecordInterface;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.UnknownRecord;

import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

/**
 *
 * Aggregate value records together.  Things are easier to handle that way.
 *
 * @author  andy
 * @author  Glen Stampoultzis (glens at apache.org)
 */

public class ValueRecordsAggregate
    extends Record
{
    public final static short sid       = -1000;
    int                       firstcell = -1;
    int                       lastcell  = -1;
    TreeMap                   records   = null;
    int                       size      = 0;

    /** Creates a new instance of ValueRecordsAggregate */

    public ValueRecordsAggregate()
    {
        records = new TreeMap();
    }

    public void insertCell(CellValueRecordInterface cell)
    {
        if (records.get(cell) == null)
        {
            size += (( Record ) cell).getRecordSize();
        }
        else
        {
            size += (( Record ) cell).getRecordSize()
                    - (( Record ) records.get(cell)).getRecordSize();
        }

        records.put(cell, cell);
        if ((cell.getColumn() < firstcell) || (firstcell == -1))
        {
            firstcell = cell.getColumn();
        }
        if ((cell.getColumn() > lastcell) || (lastcell == -1))
        {
            lastcell = cell.getColumn();
        }
    }

    public void removeCell(CellValueRecordInterface cell)
    {
        size -= (( Record ) cell).getRecordSize();

        records.remove(cell);
    }

    public int getPhysicalNumberOfCells()
    {
        return records.size();
    }

    public int getFirstCellNum()
    {
        return firstcell;
    }

    public int getLastCellNum()
    {
        return lastcell;
    }

    public int construct(int offset, List records)
    {
        int k = 0;

        for (k = offset; k < records.size(); k++)
        {
            Record rec = ( Record ) records.get(k);

            if (!rec.isInValueSection() && !(rec instanceof UnknownRecord))
            {
                break;
            }
            if (rec.isValue())
            {
                insertCell(( CellValueRecordInterface ) rec);
            }
        }
        return k;
    }

    /**
     * called by the class that is responsible for writing this sucker.
     * Subclasses should implement this so that their data is passed back in a
     * byte array.
     *
     * @param offset to begin writing at
     * @param data byte array containing instance data
     * @return number of bytes written
     */

    public int serialize(int offset, byte [] data)
    {
        Iterator itr = records.values().iterator();
        int      pos = offset;

        while (itr.hasNext())
        {
            pos += (( Record ) itr.next()).serialize(pos, data);
        }
        return pos - offset;
    }

    /**
     * called by the constructor, should set class level fields.  Should throw
     * runtime exception for bad/icomplete data.
     *
     * @param data raw data
     * @param size size of data
     * @param offset of the record's data (provided a big array of the file)
     */

    protected void fillFields(byte [] data, short size, int offset)
    {
    }

    /**
     * called by constructor, should throw runtime exception in the event of a
     * record passed with a differing ID.
     *
     * @param id alleged id for this record
     */

    protected void validateSid(short id)
    {
    }

    /**
     * return the non static version of the id for this record.
     */

    public short getSid()
    {
        return sid;
    }

    public int getRecordSize()
    {
        return size;
    }

    public Iterator getIterator()
    {
        return records.values().iterator();
    }
}

/*
 * class XYLocator implements Comparable {
 *   private int row = 0;
 *   private int col = 0;
 *   public XYLocator(int row, int col) {
 *       this.row = row;
 *       this.col = col;
 *   }
 *
 *   public int getRow() {
 *       return row;
 *   }
 *
 *   public int getCol() {
 *       return col;
 *   }
 *
 *   public int compareTo(Object obj) {
 *        XYLocator loc = (XYLocator)obj;
 *
 *        if (this.getRow() == loc.getRow() &&
 *            this.getCol() == loc.getCol() )
 *               return 0;
 *
 *        if (this.getRow() < loc.getRow())
 *               return -1;
 *
 *        if (this.getRow() > loc.getRow())
 *               return 1;
 *
 *        if (this.getCol() < loc.getCol())
 *               return -1;
 *
 *        if (this.getCol() > loc.getCol())
 *               return 1;
 *
 *        return -1;
 *
 *   }
 *
 *   public boolean equals(Object obj) {
 *       if (!(obj instanceof XYLocator)) return false;
 *
 *       XYLocator loc = (XYLocator)obj;
 *       if (this.getRow() == loc.getRow()
 *             &&
 *           this.getCol() == loc.getCol()
 *           ) return true;
 *      return false;
 *   }
 *
 *
 * }
 */
