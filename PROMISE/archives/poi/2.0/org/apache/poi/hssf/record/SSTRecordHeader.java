package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.LittleEndianConsts;

/**
 * Write out an SST header record.
 *
 * @author Glen Stampoultzis (glens at apache.org)
 */
class SSTRecordHeader
{
    int numStrings;
    int numUniqueStrings;

    public SSTRecordHeader( int numStrings, int numUniqueStrings )
    {
        this.numStrings = numStrings;
        this.numUniqueStrings = numUniqueStrings;
    }

    /**
     * Writes out the SST record.  This consists of the sid, the record size, the number of
     * strings and the number of unique strings.
     *
     * @param data          The data buffer to write the header to.
     * @param bufferIndex   The index into the data buffer where the header should be written.
     * @param recSize       The number of records written.
     *
     * @return The bufer of bytes modified.
     */
    public int writeSSTHeader( byte[] data, int bufferIndex, int recSize )
    {
        int offset = bufferIndex;

        LittleEndian.putShort( data, offset, SSTRecord.sid );
        offset += LittleEndianConsts.SHORT_SIZE;
        LittleEndian.putShort( data, offset, (short) ( recSize ) );
        offset += LittleEndianConsts.SHORT_SIZE;
        LittleEndian.putInt( data, offset, numStrings );
        offset += LittleEndianConsts.INT_SIZE;
        LittleEndian.putInt( data, offset, numUniqueStrings );
        offset += LittleEndianConsts.INT_SIZE;
        return offset - bufferIndex;
    }

}
