package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndian;

/**
 * Title:        Backup Record <P>
 * Description:  Boolean specifying whether
 *               the GUI should store a backup of the file.<P>
 * REFERENCE:  PG 287 Microsoft Excel 97 Developer's Kit (ISBN: 1-57231-498-2)<P>
 * @author Andrew C. Oliver (acoliver at apache dot org)
 * @version 2.0-pre
 */

public class BackupRecord
    extends Record
{
    public final static short sid = 0x40;

    public BackupRecord()
    {
    }

    /**
     * Constructs a BackupRecord and sets its fields appropriately
     *
     * @param id     id must be 0x40 or an exception will be throw upon validation
     * @param size  the size of the data area of the record
     * @param data  data of the record (should not contain sid/len)
     */

    public BackupRecord(short id, short size, byte [] data)
    {
        super(id, size, data);
    }

    /**
     * Constructs a BackupRecord and sets its fields appropriately
     *
     * @param id     id must be 0x40 or an exception will be throw upon validation
     * @param size  the size of the data area of the record
     * @param data  data of the record (should not contain sid/len)
     * @param offset of the start of the record's data
     */

    public BackupRecord(short id, short size, byte [] data, int offset)
    {
        super(id, size, data, offset);
    }

    protected void validateSid(short id)
    {
        if (id != sid)
        {
            throw new RecordFormatException("NOT A BACKUP RECORD");
        }
    }

    protected void fillFields(byte [] data, short size, int offset)
    {
        field_1_backup = LittleEndian.getShort(data, 0 + offset);
    }

    /**
     * set the backup flag (0,1)
     *
     * @param backup    backup flag
     */

    public void setBackup(short backup)
    {
        field_1_backup = backup;
    }

    /**
     * get the backup flag
     *
     * @return short 0/1 (off/on)
     */

    public short getBackup()
    {
        return field_1_backup;
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("[BACKUP]\n");
        buffer.append("    .backup          = ")
            .append(Integer.toHexString(getBackup())).append("\n");
        buffer.append("[/BACKUP]\n");
        return buffer.toString();
    }

    public int serialize(int offset, byte [] data)
    {
        LittleEndian.putShort(data, 0 + offset, sid);
        LittleEndian.putShort(data, 2 + offset,
        LittleEndian.putShort(data, 4 + offset, getBackup());
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
