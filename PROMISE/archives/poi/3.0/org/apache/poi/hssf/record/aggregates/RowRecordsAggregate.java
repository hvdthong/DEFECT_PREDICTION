   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at


   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */


package org.apache.poi.hssf.record.aggregates;

import org.apache.poi.hssf.record.DBCellRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.hssf.record.RowRecord;


import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author  andy
 * @author Jason Height (jheight at chariot dot net dot au)
 */

public class RowRecordsAggregate
    extends Record
{
    int     firstrow = -1;
    int     lastrow  = -1;
    Map records  = null;
    int     size     = 0;

    /** Creates a new instance of ValueRecordsAggregate */

    public RowRecordsAggregate()
    {
        records = new TreeMap();
    }

    public void insertRow(RowRecord row)
    {
        size += row.getRecordSize();

        records.put(row, row);
        if ((row.getRowNumber() < firstrow) || (firstrow == -1))
        {
            firstrow = row.getRowNumber();
        }
        if ((row.getRowNumber() > lastrow) || (lastrow == -1))
        {
            lastrow = row.getRowNumber();
        }
    }

    public void removeRow(RowRecord row)
    {
        size -= row.getRecordSize();

        records.remove(row);
    }

    public RowRecord getRow(int rownum)
    {

        RowRecord row = new RowRecord();

        row.setRowNumber(( short ) rownum);
        return ( RowRecord ) records.get(row);
    }

    public int getPhysicalNumberOfRows()
    {
        return records.size();
    }

    public int getFirstRowNum()
    {
        return firstrow;
    }

    public int getLastRowNum()
    {
        return lastrow;
    }
    
    /** Returns the number of row blocks.
     * <p/>The row blocks are goupings of rows that contain the DBCell record
     * after them
     */
    public int getRowBlockCount() {
      int size = records.size()/DBCellRecord.BLOCK_SIZE;
      if ((records.size() % DBCellRecord.BLOCK_SIZE) != 0)
          size++;
      return size;
    }

    public int getRowBlockSize(int block) {
      return 20 * getRowCountForBlock(block);
    }

    /** Returns the number of physical rows within a block*/
    public int getRowCountForBlock(int block) {
      int startIndex = block * DBCellRecord.BLOCK_SIZE;
      int endIndex = startIndex + DBCellRecord.BLOCK_SIZE - 1;
      if (endIndex >= records.size())
        endIndex = records.size()-1;

      return endIndex-startIndex+1;
    }

    /** Returns the physical row number of the first row in a block*/
    public int getStartRowNumberForBlock(int block) {
      int startIndex = block * DBCellRecord.BLOCK_SIZE;
      Iterator rowIter = records.values().iterator();
      RowRecord row = null;
      for (int i=0; i<=startIndex;i++) {
        row = (RowRecord)rowIter.next();
      }

      return row.getRowNumber();
    }

    /** Returns the physical row number of the end row in a block*/
    public int getEndRowNumberForBlock(int block) {
      int endIndex = ((block + 1)*DBCellRecord.BLOCK_SIZE)-1;
      if (endIndex >= records.size())
        endIndex = records.size()-1;

      Iterator rowIter = records.values().iterator();
      RowRecord row = null;
      for (int i=0; i<=endIndex;i++) {
        row = (RowRecord)rowIter.next();
      }
      return row.getRowNumber();
    }


    /** Serializes a block of the rows */
    private int serializeRowBlock(final int block, final int offset, byte[] data) {
      final int startIndex = block*DBCellRecord.BLOCK_SIZE;
      final int endIndex = startIndex + DBCellRecord.BLOCK_SIZE;

      Iterator rowIterator = records.values().iterator();
      int pos = offset;

      int i=0;
      for (;i<startIndex;i++)
        rowIterator.next();
      while(rowIterator.hasNext() && (i++ < endIndex)) {
        RowRecord row = (RowRecord)rowIterator.next();
        pos += row.serialize(pos, data);
      }
      return pos - offset;
    }

    public int serialize(int offset, byte [] data) {
      throw new RuntimeException("The serialize method that passes in cells should be used");
    }
    

    /**
     * called by the class that is responsible for writing this sucker.
     * Subclasses should implement this so that their data is passed back in a
     * byte array.
     *
     * @param offset    offset to begin writing at
     * @param data      byte array containing instance data
     * @return number of bytes written
     */

    public int serialize(int offset, byte [] data, ValueRecordsAggregate cells)
    {
        int pos = offset;

        final int blockCount = getRowBlockCount();
        for (int block=0;block<blockCount;block++) {
          final int rowStartPos = pos;
          final int rowBlockSize = serializeRowBlock(block, pos, data);
          pos += rowBlockSize;
          final int startRowNumber = getStartRowNumberForBlock(block);
          final int endRowNumber = getEndRowNumberForBlock(block);
          DBCellRecord cellRecord = new DBCellRecord();
          int cellRefOffset = (rowBlockSize-20);
          for (int row=startRowNumber;row<=endRowNumber;row++) {
            if (null != cells && cells.rowHasCells(row)) {
              final int rowCellSize = cells.serializeCellRow(row, pos, data);
              pos += rowCellSize;
              cellRecord.addCellOffset((short)cellRefOffset);
              cellRefOffset = rowCellSize;
            }
          }
          cellRecord.setRowOffset(pos - rowStartPos);
          pos += cellRecord.serialize(pos, data);

        }
        return pos - offset;
    }

    /**
     * called by the constructor, should set class level fields.  Should throw
     * runtime exception for bad/incomplete data.
     *
     * @param data raw data
     * @param size size of data
     * @param offset of the record's data (provided a big array of the file)
     */

    protected void fillFields(RecordInputStream in)
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
        return -1000;
    }

    public int getRecordSize()
    {
        return size;
    }

    public Iterator getIterator()
    {
        return records.values().iterator();
    }
    
    /**
     * Performs a deep clone of the record
     */
    public Object clone()
    {
        RowRecordsAggregate rec = new RowRecordsAggregate();
        for ( Iterator rowIter = getIterator(); rowIter.hasNext(); )
        {
            RowRecord row = (RowRecord) ( (RowRecord) rowIter.next() ).clone();
            rec.insertRow( row );
        }
        return rec;
    }


    public int findStartOfRowOutlineGroup(int row)
    {
        RowRecord rowRecord = this.getRow( row );
        int level = rowRecord.getOutlineLevel();
        int currentRow = row;
        while (this.getRow( currentRow ) != null)
        {
            rowRecord = this.getRow( currentRow );
            if (rowRecord.getOutlineLevel() < level)
                return currentRow + 1;
            currentRow--;
        }

        return currentRow + 1;
    }

    public int findEndOfRowOutlineGroup( int row )
    {
        int level = getRow( row ).getOutlineLevel();
        int currentRow;
        for (currentRow = row; currentRow < this.getLastRowNum(); currentRow++)
        {
            if (getRow(currentRow) == null || getRow(currentRow).getOutlineLevel() < level)
            {
                break;
            }
        }

        return currentRow-1;
    }

    public int writeHidden( RowRecord rowRecord, int row, boolean hidden )
    {
        int level = rowRecord.getOutlineLevel();
        while (rowRecord != null && this.getRow(row).getOutlineLevel() >= level)
        {
            rowRecord.setZeroHeight( hidden );
            row++;
            rowRecord = this.getRow( row );
        }
        return row - 1;
    }

    public void collapseRow( int rowNumber )
    {

        int startRow = findStartOfRowOutlineGroup( rowNumber );
        RowRecord rowRecord = (RowRecord) getRow( startRow );

        int lastRow = writeHidden( rowRecord, startRow, true );

        if (getRow(lastRow + 1) != null)
        {
            getRow(lastRow + 1).setColapsed( true );
        }
        else
        {
            RowRecord row = createRow( lastRow + 1);
            row.setColapsed( true );
            insertRow( row );
        }
    }

    /**
     * Create a row record.
     *
     * @param row number
     * @return RowRecord created for the passed in row number
     * @see org.apache.poi.hssf.record.RowRecord
     */
    public static RowRecord createRow(int row)
    {
        RowRecord rowrec = new RowRecord();

        rowrec.setRowNumber(row);
        rowrec.setHeight(( short ) 0xff);
        rowrec.setOptimize(( short ) 0x0);
        rowrec.setXFIndex(( short ) 0xf);
        return rowrec;
    }

    public boolean isRowGroupCollapsed( int row )
    {
        int collapseRow = findEndOfRowOutlineGroup( row ) + 1;

        if (getRow(collapseRow) == null)
            return false;
        else
            return getRow( collapseRow ).getColapsed();
    }

    public void expandRow( int rowNumber )
    {
        int idx = rowNumber;
        if (idx == -1)
            return;

        if (!isRowGroupCollapsed(idx))
            return;

        int startIdx = findStartOfRowOutlineGroup( idx );
        RowRecord row = getRow( startIdx );

        int endIdx = findEndOfRowOutlineGroup( idx );

        if ( !isRowGroupHiddenByParent( idx ) )
        {
            for ( int i = startIdx; i <= endIdx; i++ )
            {
                if ( row.getOutlineLevel() == getRow( i ).getOutlineLevel() )
                    getRow( i ).setZeroHeight( false );
                else if (!isRowGroupCollapsed(i))
                    getRow( i ).setZeroHeight( false );
            }
        }

        getRow( endIdx + 1 ).setColapsed( false );
    }

    public boolean isRowGroupHiddenByParent( int row )
    {
        int endLevel;
        boolean endHidden;
        int endOfOutlineGroupIdx = findEndOfRowOutlineGroup( row );
        if (getRow( endOfOutlineGroupIdx + 1 ) == null)
        {
            endLevel = 0;
            endHidden = false;
        }
        else
        {
            endLevel = getRow( endOfOutlineGroupIdx + 1).getOutlineLevel();
            endHidden = getRow( endOfOutlineGroupIdx + 1).getZeroHeight();
        }

        int startLevel;
        boolean startHidden;
        int startOfOutlineGroupIdx = findStartOfRowOutlineGroup( row );
        if (startOfOutlineGroupIdx - 1 < 0 || getRow(startOfOutlineGroupIdx - 1) == null)
        {
            startLevel = 0;
            startHidden = false;
        }
        else
        {
            startLevel = getRow( startOfOutlineGroupIdx - 1).getOutlineLevel();
            startHidden = getRow( startOfOutlineGroupIdx - 1 ).getZeroHeight();
        }

        if (endLevel > startLevel)
        {
            return endHidden;
        }
        else
        {
            return startHidden;
        }
    }

}

