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
        

package org.apache.poi.hssf.record;

import org.apache.poi.util.IntList;
import org.apache.poi.util.LittleEndian;

/**
 * Title:        Index Record<P>
 * Description:  Occurs right after BOF, tells you where the DBCELL records are for a sheet
 *               Important for locating cells<P>
 * NOT USED IN THIS RELEASE
 * REFERENCE:  PG 323 Microsoft Excel 97 Developer's Kit (ISBN: 1-57231-498-2)<P>
 * @author Andrew C. Oliver (acoliver at apache dot org)
 * @author Jason Height (jheight at chariot dot net dot au)
 * @version 2.0-pre
 */

public class IndexRecord
    extends Record
{
    public final static short sid             = 0x20B;
    public final static int   DBCELL_CAPACITY = 30;

    public IndexRecord()
    {
    }

    /**
     * Constructs an Index record and sets its fields appropriately.
     *
     * @param id     id must be 0x208 or an exception will be throw upon validation
     * @param size  the size of the data area of the record
     * @param data  data of the record (should not contain sid/len)
     */

    public IndexRecord(RecordInputStream in)
    {
        super(in);
    }

    protected void validateSid(short id)
    {
        if (id != sid)
        {
            throw new RecordFormatException("NOT An Index RECORD");
        }
    }

    protected void fillFields(RecordInputStream in)
    {
        field_5_dbcells       =
        field_1_zero          = in.readInt();
        field_2_first_row     = in.readInt();
        field_3_last_row_add1 = in.readInt();
        field_4_zero          = in.readInt();
        while(in.remaining() > 0)
        {

            field_5_dbcells.add(in.readInt());
        }
    }

    public void setFirstRow(int row)
    {
        field_2_first_row = row;
    }

    public void setLastRowAdd1(int row)
    {
        field_3_last_row_add1 = row;
    }

    public void addDbcell(int cell)
    {
        if (field_5_dbcells == null)
        {
            field_5_dbcells = new IntList();
        }
        field_5_dbcells.add(cell);
    }

    public void setDbcell(int cell, int value)
    {
        field_5_dbcells.set(cell, value);
    }

    public int getFirstRow()
    {
        return field_2_first_row;
    }

    public int getLastRowAdd1()
    {
        return field_3_last_row_add1;
    }

    public int getNumDbcells()
    {
        if (field_5_dbcells == null)
        {
            return 0;
        }
        return field_5_dbcells.size();
    }

    public int getDbcellAt(int cellnum)
    {
        return field_5_dbcells.get(cellnum);
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("[INDEX]\n");
        buffer.append("    .firstrow       = ")
            .append(Integer.toHexString(getFirstRow())).append("\n");
        buffer.append("    .lastrowadd1    = ")
            .append(Integer.toHexString(getLastRowAdd1())).append("\n");
        for (int k = 0; k < getNumDbcells(); k++)
        {
            buffer.append("    .dbcell_" + k + "       = ")
                .append(Integer.toHexString(getDbcellAt(k))).append("\n");
        }
        buffer.append("[/INDEX]\n");
        return buffer.toString();
    }

    public int serialize(int offset, byte [] data)
    {
        LittleEndian.putShort(data, 0 + offset, sid);
        LittleEndian.putShort(data, 2 + offset,
                              ( short ) (16 + (getNumDbcells() * 4)));
        LittleEndian.putInt(data, 4 + offset, 0);
        LittleEndian.putInt(data, 8 + offset, getFirstRow());
        LittleEndian.putInt(data, 12 + offset, getLastRowAdd1());
        LittleEndian.putInt(data, 16 + offset, 0);
        for (int k = 0; k < getNumDbcells(); k++)
        {
            LittleEndian.putInt(data, (k * 4) + 20 + offset, getDbcellAt(k));
        }
        return getRecordSize();
    }

    public int getRecordSize()
    {
        return 20 + (getNumDbcells() * 4);
    }
    
    /** Returns the size of an INdexRecord when it needs to index the specified number of blocks
      *
      */
     public static int getRecordSizeForBlockCount(int blockCount) {
       return 20 + (4 * blockCount);
     }  

    public short getSid()
    {
        return sid;
    }

    public Object clone() {
      IndexRecord rec = new IndexRecord();
      rec.field_1_zero = field_1_zero;
      rec.field_2_first_row = field_2_first_row;
      rec.field_3_last_row_add1 = field_3_last_row_add1;
      rec.field_4_zero = field_4_zero;
      rec.field_5_dbcells = new IntList();
      rec.field_5_dbcells.addAll(field_5_dbcells);
      return rec;
    }
}
