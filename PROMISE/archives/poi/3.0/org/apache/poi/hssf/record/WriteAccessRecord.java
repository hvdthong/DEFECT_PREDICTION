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

import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.StringUtil;

/**
 * Title:        Write Access Record<P>
 * Description:  Stores the username of that who owns the spreadsheet generator
 *               (on unix the user's login, on Windoze its the name you typed when
 *                you installed the thing)<P>
 * REFERENCE:  PG 424 Microsoft Excel 97 Developer's Kit (ISBN: 1-57231-498-2)<P>
 * @author Andrew C. Oliver (acoliver at apache dot org)
 * @version 2.0-pre
 */

public class WriteAccessRecord
    extends Record
{
    public final static short sid = 0x5c;
    private String            field_1_username;

    public WriteAccessRecord()
    {
    }

    /**
     * Constructs a WriteAccess record and sets its fields appropriately.
     *
     * @param id     id must be 0x5c or an exception will be throw upon validation
     * @param size  the size of the data area of the record
     * @param data  data of the record (should not contain sid/len)
     */

    public WriteAccessRecord(RecordInputStream in)
    {
        super(in);
    }

    protected void validateSid(short id)
    {
        if (id != sid)
        {
            throw new RecordFormatException("NOT A WRITEACCESS RECORD");
        }
    }

    protected void fillFields(RecordInputStream in)
    {
        byte[] data = in.readRemainder();

        field_1_username = StringUtil.getFromCompressedUnicode(data, 3, data.length - 3);
    }

    /**
     * set the username for the user that created the report.  HSSF uses the logged in user.
     * @param username of the user who  is logged in (probably "tomcat" or "apache")
     */

    public void setUsername(String username)
    {
        field_1_username = username;
    }

    /**
     * get the username for the user that created the report.  HSSF uses the logged in user.  On
     * natively created M$ Excel sheet this would be the name you typed in when you installed it
     * in most cases.
     * @return username of the user who  is logged in (probably "tomcat" or "apache")
     */

    public String getUsername()
    {
        return field_1_username;
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("[WRITEACCESS]\n");
        buffer.append("    .name            = ")
            .append(field_1_username.toString()).append("\n");
        buffer.append("[/WRITEACCESS]\n");
        return buffer.toString();
    }

    public int serialize(int offset, byte [] data)
    {
        String       username = getUsername();
        StringBuffer temp     = new StringBuffer(0x70 - (0x3));

        temp.append(username);
        while (temp.length() < 0x70 - 0x3)
        {
            temp.append(
        }
        username = temp.toString();
        UnicodeString str = new UnicodeString(username);
        str.setOptionFlags(( byte ) 0x0);

        LittleEndian.putShort(data, 0 + offset, sid);
        UnicodeString.UnicodeRecordStats stats = new UnicodeString.UnicodeRecordStats();
        stats.recordSize += 4;
        stats.remainingSize-= 4;
        str.serialize(stats, 4 + offset, data);

        return getRecordSize();
    }

    public int getRecordSize()
    {
        return 116;
    }

    public short getSid()
    {
        return sid;
    }
}
