package org.apache.poi.hssf.record;

import org.apache.poi.util.BinaryTree;
import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.LittleEndianConsts;

import java.util.*;

/**
 * Title:        Static String Table Record
 * <P>
 * Description:  This holds all the strings for LabelSSTRecords.
 * <P>
 * REFERENCE:    PG 389 Microsoft Excel 97 Developer's Kit (ISBN:
 *               1-57231-498-2)
 * <P>
 * @author Andrew C. Oliver (acoliver at apache dot org)
 * @author Marc Johnson (mjohnson at apache dot org)
 * @version 2.0-pre
 * @see org.apache.poi.hssf.record.LabelSSTRecord
 * @see org.apache.poi.hssf.record.ContinueRecord
 */

public class SSTRecord
    extends Record
{

    private static final int  _max                     = 8228;

    private static final int  _std_record_overhead     =
        2 * LittleEndianConsts.SHORT_SIZE;

    private static final int  _sst_record_overhead     =
        (_std_record_overhead + (2 * LittleEndianConsts.INT_SIZE));

    private static final int  _max_data_space          =
        _max - _sst_record_overhead;

    private static final int  _string_minimal_overhead =
        LittleEndianConsts.SHORT_SIZE + LittleEndianConsts.BYTE_SIZE;
    public static final short sid                      = 0xfc;

    private int               field_1_num_strings;

    private int               field_2_num_unique_strings;
    private BinaryTree        field_3_strings;

    private int               __expected_chars;

    private String            _unfinished_string;

    private int               _total_length_bytes;

    private int               _string_data_offset;

    private boolean           _wide_char;
    private List              _record_lengths = null;

    /**
     * default constructor
     */

    public SSTRecord()
    {
        field_1_num_strings        = 0;
        field_2_num_unique_strings = 0;
        field_3_strings            = new BinaryTree();
        setExpectedChars(0);
        _unfinished_string  = "";
        _total_length_bytes = 0;
        _string_data_offset = 0;
        _wide_char          = false;
    }

    /**
     * Constructs an SST record and sets its fields appropriately.
     *
     * @param id must be 0xfc or an exception will be throw upon
     *           validation
     * @param size the size of the data area of the record
     * @param data of the record (should not contain sid/len)
     */

    public SSTRecord(final short id, final short size, final byte [] data)
    {
        super(id, size, data);
    }

    /**
     * Constructs an SST record and sets its fields appropriately.
     *
     * @param id must be 0xfc or an exception will be throw upon
     *           validation
     * @param size the size of the data area of the record
     * @param data of the record (should not contain sid/len)
     * @param offset of the record
     */

    public SSTRecord(final short id, final short size, final byte [] data,
                     int offset)
    {
        super(id, size, data, offset);
    }

    /**
     * Add a string. Determines whether 8-bit encoding can be used, or
     * whether 16-bit encoding must be used.
     * <p>
     * THIS IS THE PREFERRED METHOD OF ADDING A STRING. IF YOU USE THE
     * OTHER ,code>addString</code> METHOD AND FORCE 8-BIT ENCODING ON
     * A STRING THAT SHOULD USE 16-BIT ENCODING, YOU WILL CORRUPT THE
     * STRING; IF YOU USE THAT METHOD AND FORCE 16-BIT ENCODING, YOU
     * ARE WASTING SPACE WHEN THE WORKBOOK IS WRITTEN OUT.
     *
     * @param string string to be added
     *
     * @return the index of that string in the table
     */

    public int addString(final String string)
    {
        int rval;

        if (string == null)
        {
            rval = addString("", false);
        }
        else
        {

            boolean useUTF16 = false;
            int     strlen   = string.length();

            for (int j = 0; j < strlen; j++)
            {
                if (string.charAt(j) > 255)
                {
                    useUTF16 = true;
                    break;
                }
            }
            rval = addString(string, useUTF16);
        }
        return rval;
    }

    /**
     * Add a string and assert the encoding (8-bit or 16-bit) to be
     * used.
     * <P>
     * USE THIS METHOD AT YOUR OWN RISK. IF YOU FORCE 8-BIT ENCODING,
     * YOU MAY CORRUPT YOUR STRING. IF YOU FORCE 16-BIT ENCODING AND
     * IT ISN'T NECESSARY, YOU WILL WASTE SPACE WHEN THIS RECORD IS
     * WRITTEN OUT.
     *
     * @param string string to be added
     * @param useUTF16 if true, forces 16-bit encoding. If false,
     *                 forces 8-bit encoding
     *
     * @return the index of that string in the table
     */

    public int addString(final String string, final boolean useUTF16)
    {
        field_1_num_strings++;
        String        str  = (string == null) ? ""
                                              : string;
        int           rval = -1;
        UnicodeString ucs  = new UnicodeString();

        ucs.setString(str);
        ucs.setCharCount(( short ) str.length());
        ucs.setOptionFlags(( byte ) (useUTF16 ? 1
                                              : 0));
        Integer integer = ( Integer ) field_3_strings.getKeyForValue(ucs);

        if (integer != null)
        {
            rval = integer.intValue();
        }
        else
        {

            rval = field_3_strings.size();
            field_2_num_unique_strings++;
            integer = new Integer(rval);
            field_3_strings.put(integer, ucs);
        }
        return rval;
    }

    /**
     * @return number of strings
     */

    public int getNumStrings()
    {
        return field_1_num_strings;
    }

    /**
     * @return number of unique strings
     */

    public int getNumUniqueStrings()
    {
        return field_2_num_unique_strings;
    }

    /**
     * USE THIS METHOD AT YOUR OWN PERIL: THE <code>addString</code>
     * METHODS MANIPULATE THE NUMBER OF STRINGS AS A SIDE EFFECT; YOUR
     * ATTEMPTS AT MANIPULATING THE STRING COUNT IS LIKELY TO BE VERY
     * WRONG AND WILL RESULT IN BAD BEHAVIOR WHEN THIS RECORD IS
     * WRITTEN OUT AND ANOTHER PROCESS ATTEMPTS TO READ THE RECORD
     *
     * @param count  number of strings
     *
     */

    public void setNumStrings(final int count)
    {
        field_1_num_strings = count;
    }

    /**
     * USE THIS METHOD AT YOUR OWN PERIL: THE <code>addString</code>
     * METHODS MANIPULATE THE NUMBER OF UNIQUE STRINGS AS A SIDE
     * EFFECT; YOUR ATTEMPTS AT MANIPULATING THE UNIQUE STRING COUNT
     * IS LIKELY TO BE VERY WRONG AND WILL RESULT IN BAD BEHAVIOR WHEN
     * THIS RECORD IS WRITTEN OUT AND ANOTHER PROCESS ATTEMPTS TO READ
     * THE RECORD
     *
     * @param count  number of strings
     */

    public void getNumUniqueStrings(final int count)
    {
        field_2_num_unique_strings = count;
    }

    /**
     * Get a particular string by its index
     *
     * @param id index into the array of strings
     *
     * @return the desired string
     */

    public String getString(final int id)
    {
        return (( UnicodeString ) field_3_strings.get(new Integer(id)))
            .getString();
    }

    public boolean getString16bit(final int id)
    {
        return ((( UnicodeString ) field_3_strings.get(new Integer(id)))
            .getOptionFlags() == 1);
    }

    /**
     * Return a debugging string representation
     *
     * @return string representation
     */

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("[SST]\n");
        buffer.append("    .numstrings     = ")
            .append(Integer.toHexString(getNumStrings())).append("\n");
        buffer.append("    .uniquestrings  = ")
            .append(Integer.toHexString(getNumUniqueStrings())).append("\n");
        for (int k = 0; k < field_3_strings.size(); k++)
        {
            buffer.append("    .string_" + k + "      = ")
                .append((( UnicodeString ) field_3_strings
                .get(new Integer(k))).toString()).append("\n");
        }
        buffer.append("[/SST]\n");
        return buffer.toString();
    }

    /**
     * Create a byte array consisting of an SST record and any
     * required Continue records, ready to be written out.
     * <p>
     * If an SST record and any subsequent Continue records are read
     * in to create this instance, this method should produce a byte
     * array that is identical to the byte array produced by
     * concatenating the input records' data.
     *
     * @return the byte array
     */

    public int serialize(int offset, byte [] data)
    {
        int rval                = getRecordSize();
        int record_length_index = 0;

        int unicodesize         = calculateUnicodeSize();

        if (unicodesize > _max_data_space)
        {
            byte[]  stringreminant     = null;
            int     unipos             = 0;
            boolean lastneedcontinue   = false;
            int     stringbyteswritten = 0;
            boolean first_record       = true;
            int     totalWritten       = 0;
            int     size               = 0;

            while (totalWritten != rval)
            {
                int pos = 0;

                int available;

                if (first_record)
                {
                    size         =
                        (( Integer ) _record_lengths
                            .get(record_length_index++)).intValue();
                    available    = size - 8;
                    pos          = writeSSTHeader(data,
                                                  pos + offset
                                                  + totalWritten, size);
                    size         += _std_record_overhead;
                    first_record = false;
                }
                else
                {
                    pos = 0;
                    int to_be_written = (unicodesize - stringbyteswritten)
                                        + (lastneedcontinue ? 1

                    size      =
                        (( Integer ) _record_lengths
                            .get(record_length_index++)).intValue();
                    available = size;
                    pos       = writeContinueHeader(data,
                                                    pos + offset
                                                    + totalWritten, size);
                    size      = size + _std_record_overhead;
                }

                if (lastneedcontinue)
                {

                    if (stringreminant.length <= available)
                    {

                        System.arraycopy(stringreminant, 0, data,
                                         pos + offset + totalWritten,
                                         stringreminant.length);
                        stringbyteswritten += stringreminant.length - 1;
                        pos                += stringreminant.length;
                        lastneedcontinue   = false;
                        available          -= stringreminant.length;
                    }
                    else
                    {

                        System.arraycopy(stringreminant, 0, data,
                                         pos + offset + totalWritten,
                                         available);
                        stringbyteswritten += available - 1;
                        pos                += available;
                        byte[] leftover =
                            new byte[ (stringreminant.length - available) + LittleEndianConsts.BYTE_SIZE ];

                        System.arraycopy(stringreminant, available, leftover,
                                         LittleEndianConsts.BYTE_SIZE,
                                         stringreminant.length - available);
                        leftover[ 0 ]    = stringreminant[ 0 ];
                        stringreminant   = leftover;
                        available        = 0;
                        lastneedcontinue = true;
                    }
                }

                for (; unipos < field_3_strings.size(); unipos++)
                {
                    Integer       intunipos = new Integer(unipos);
                    UnicodeString unistr    =
                        (( UnicodeString ) field_3_strings.get(intunipos));

                    if (unistr.getRecordSize() <= available)
                    {
                        unistr.serialize(pos + offset + totalWritten, data);
                        int rsize = unistr.getRecordSize();

                        stringbyteswritten += rsize;
                        pos                += rsize;
                        available          -= rsize;
                    }
                    else
                    {

                        if (available >= _string_minimal_overhead)
                        {

                            byte[] ucs = unistr.serialize();

                            System.arraycopy(ucs, 0, data,
                                             pos + offset + totalWritten,
                                             available);
                            stringbyteswritten += available;
                            stringreminant     =
                                new byte[ (ucs.length - available) + LittleEndianConsts.BYTE_SIZE ];
                            System.arraycopy(ucs, available, stringreminant,
                                             LittleEndianConsts.BYTE_SIZE,
                                             ucs.length - available);
                            stringreminant[ 0 ] =
                                ucs[ LittleEndianConsts.SHORT_SIZE ];
                            available           = 0;
                            lastneedcontinue    = true;
                            unipos++;
                        }
                        break;
                    }
                }
                totalWritten += size;
            }
        }
        else
        {


            writeSSTHeader(
                data, 0 + offset,
                _sst_record_overhead
                + (( Integer ) _record_lengths.get(
                record_length_index++)).intValue() - _std_record_overhead);
            int pos = _sst_record_overhead;

            for (int k = 0; k < field_3_strings.size(); k++)
            {
                UnicodeString unistr =
                    (( UnicodeString ) field_3_strings.get(new Integer(k)));

                System.arraycopy(unistr.serialize(), 0, data, pos + offset,
                                 unistr.getRecordSize());
                pos += unistr.getRecordSize();
            }
        }
        return rval;
    }

    private int calculateStringsize()
    {
        int retval = 0;

        for (int k = 0; k < field_3_strings.size(); k++)
        {
            retval +=
                (( UnicodeString ) field_3_strings.get(new Integer(k)))
                    .getRecordSize();
        }
        return retval;
    }

    /**
     * Process a Continue record. A Continue record for an SST record
     * contains the same kind of data that the SST record contains,
     * with the following exceptions:
     * <P>
     * <OL>
     * <LI>The string counts at the beginning of the SST record are
     *     not in the Continue record
     * <LI>The first string in the Continue record might NOT begin
     *     with a size. If the last string in the previous record is
     *     continued in this record, the size is determined by that
     *     last string in the previous record; the first string will
     *     begin with a flag byte, followed by the remaining bytes (or
     *     words) of the last string from the previous
     *     record. Otherwise, the first string in the record will
     *     begin with a string length
     * </OL>
     *
     * @param record the Continue record's byte data
     */

    public void processContinueRecord(final byte [] record)
    {
        if (getExpectedChars() == 0)
        {
            _unfinished_string  = "";
            _total_length_bytes = 0;
            _string_data_offset = 0;
            _wide_char          = false;
            manufactureStrings(record, 0, ( short ) record.length);
        }
        else
        {
            int data_length = record.length - LittleEndianConsts.BYTE_SIZE;

            if (calculateByteCount(getExpectedChars()) > data_length)
            {

                byte[] input =
                    new byte[ record.length + LittleEndianConsts.SHORT_SIZE ];
                short  size  = ( short ) (((record[ 0 ] & 1) == 1)
                                          ? (data_length
                                             / LittleEndianConsts.SHORT_SIZE)
                                          : (data_length
                                             / LittleEndianConsts.BYTE_SIZE));

                LittleEndian.putShort(input, ( byte ) 0, size);
                System.arraycopy(record, 0, input,
                                 LittleEndianConsts.SHORT_SIZE,
                                 record.length);
                UnicodeString ucs = new UnicodeString(UnicodeString.sid,
                                                      ( short ) input.length,
                                                      input);

                _unfinished_string = _unfinished_string + ucs.getString();
                setExpectedChars(getExpectedChars() - size);
            }
            else
            {
                setupStringParameters(record, -LittleEndianConsts.SHORT_SIZE,
                                      getExpectedChars());
                byte[] str_data = new byte[ _total_length_bytes ];
                int    length   = _string_minimal_overhead
                                  + (calculateByteCount(getExpectedChars()));
                byte[] bstring  = new byte[ length ];

                System.arraycopy(record, 0, str_data,
                                 LittleEndianConsts.SHORT_SIZE,
                                 str_data.length
                                 - LittleEndianConsts.SHORT_SIZE);

                LittleEndian.putShort(bstring, 0,
                                      ( short ) getExpectedChars());

                bstring[ LittleEndianConsts.SHORT_SIZE ] =
                    str_data[ LittleEndianConsts.SHORT_SIZE ];

                System.arraycopy(str_data, _string_data_offset, bstring,
                                 _string_minimal_overhead,
                                 bstring.length - _string_minimal_overhead);

                UnicodeString string  =
                    new UnicodeString(UnicodeString.sid,
                                      ( short ) bstring.length, bstring,
                                      _unfinished_string);
                Integer       integer = new Integer(field_3_strings.size());

                field_3_strings.put(integer, string);
                manufactureStrings(record,
                                   _total_length_bytes
                                   - LittleEndianConsts
                                       .SHORT_SIZE, ( short ) record.length);
            }
        }
    }

    /**
     * @return sid
     */

    public short getSid()
    {
        return sid;
    }

    /**
     * @return hashcode
     */

    public int hashCode()
    {
        return field_2_num_unique_strings;
    }

    /**
     *
     * @param o
     * @return true if equal
     */

    public boolean equals(Object o)
    {
        if ((o == null) || (o.getClass() != this.getClass()))
        {
            return false;
        }
        SSTRecord other = ( SSTRecord ) o;

        return ((field_1_num_strings == other
            .field_1_num_strings) && (field_2_num_unique_strings == other
                .field_2_num_unique_strings) && field_3_strings
                    .equals(other.field_3_strings));
    }

    /**
     * validate SID
     *
     * @param id the alleged SID
     *
     * @exception RecordFormatException if validation fails
     */

    protected void validateSid(final short id)
        throws RecordFormatException
    {
        if (id != sid)
        {
            throw new RecordFormatException("NOT An SST RECORD");
        }
    }

    /**
     * Fill the fields from the data
     * <P>
     * The data consists of sets of string data. This string data is
     * arranged as follows:
     * <P>
     * <CODE>
     * </CODE>
     * <P>
     * The string_flag is bit mapped as follows:
     * <P>
     * <TABLE>
     *   <TR>
     *      <TH>Bit number</TH>
     *      <TH>Meaning if 0</TH>
     *      <TH>Meaning if 1</TH>
     *   <TR>
     *   <TR>
     *      <TD>0</TD>
     *      <TD>string_data is byte[]</TD>
     *      <TD>string_data is short[]</TH>
     *   <TR>
     *   <TR>
     *      <TD>1</TD>
     *      <TD>Should always be 0</TD>
     *      <TD>string_flag is defective</TH>
     *   <TR>
     *   <TR>
     *      <TD>2</TD>
     *      <TD>extension is not included</TD>
     *      <TD>extension is included</TH>
     *   <TR>
     *   <TR>
     *      <TD>3</TD>
     *      <TD>formatting run data is not included</TD>
     *      <TD>formatting run data is included</TH>
     *   <TR>
     *   <TR>
     *      <TD>4</TD>
     *      <TD>Should always be 0</TD>
     *      <TD>string_flag is defective</TH>
     *   <TR>
     *   <TR>
     *      <TD>5</TD>
     *      <TD>Should always be 0</TD>
     *      <TD>string_flag is defective</TH>
     *   <TR>
     *   <TR>
     *      <TD>6</TD>
     *      <TD>Should always be 0</TD>
     *      <TD>string_flag is defective</TH>
     *   <TR>
     *   <TR>
     *      <TD>7</TD>
     *      <TD>Should always be 0</TD>
     *      <TD>string_flag is defective</TH>
     *   <TR>
     * </TABLE>
     * <P>
     * We can handle eating the overhead associated with bits 2 or 3
     * (or both) being set, but we have no idea what to do with the
     * associated data. The UnicodeString class can handle the byte[]
     * vs short[] nature of the actual string data
     *
     * @param data raw data
     * @param size size of the raw data
     */

    protected void fillFields(final byte [] data, final short size,
                              int offset)
    {

        field_1_num_strings        = LittleEndian.getInt(data, 0 + offset);
        field_2_num_unique_strings = LittleEndian.getInt(data, 4 + offset);
        field_3_strings            = new BinaryTree();
        setExpectedChars(0);
        _unfinished_string  = "";
        _total_length_bytes = 0;
        _string_data_offset = 0;
        _wide_char          = false;
        manufactureStrings(data, 8 + offset, size);
    }

    /**
     * @return the number of characters we expect in the first
     *         sub-record in a subsequent continuation record
     */

    int getExpectedChars()
    {
        return __expected_chars;
    }

    /**
     * @return an iterator of the strings we hold. All instances are
     *         UnicodeStrings
     */

    Iterator getStrings()
    {
        return field_3_strings.values().iterator();
    }

    /**
     * @return count of the strings we hold.
     */

    int countStrings()
    {
        return field_3_strings.size();
    }

    /**
     * @return the unfinished string
     */

    String getUnfinishedString()
    {
        return _unfinished_string;
    }

    /**
     * @return the total length of the current string
     */

    int getTotalLength()
    {
        return _total_length_bytes;
    }

    /**
     * @return offset into current string data
     */

    int getStringDataOffset()
    {
        return _string_data_offset;
    }

    /**
     * @return true if current string uses wide characters
     */

    boolean isWideChar()
    {
        return _wide_char;
    }

    private int writeSSTHeader(final byte [] data, final int pos,
                               final int recsize)
    {
        int offset = pos;

        LittleEndian.putShort(data, offset, sid);
        offset += LittleEndianConsts.SHORT_SIZE;
        LittleEndian.putShort(data, offset, ( short ) (recsize));
        offset += LittleEndianConsts.SHORT_SIZE;
        LittleEndian.putInt(data, offset, getNumStrings());
        offset += LittleEndianConsts.INT_SIZE;
        LittleEndian.putInt(data, offset, getNumUniqueStrings());
        offset += LittleEndianConsts.INT_SIZE;
        return offset - pos;
    }

    private int writeContinueHeader(final byte [] data, final int pos,
                                    final int recsize)
    {
        int offset = pos;

        LittleEndian.putShort(data, offset, ContinueRecord.sid);
        offset += LittleEndianConsts.SHORT_SIZE;
        LittleEndian.putShort(data, offset, ( short ) (recsize));
        offset += LittleEndianConsts.SHORT_SIZE;
        return offset - pos;
    }

    private int calculateUCArrayLength(final byte [][] ucarray)
    {
        int retval = 0;

        for (int k = 0; k < ucarray.length; k++)
        {
            retval += ucarray[ k ].length;
        }
        return retval;
    }

    private void manufactureStrings(final byte [] data, final int index,
                                    short size)
    {
        int offset = index;

        while (offset < size)
        {
            int remaining = size - offset;

            if ((remaining > 0)
                    && (remaining < LittleEndianConsts.SHORT_SIZE))
            {
                throw new RecordFormatException(
                    "Cannot get length of the last string in SSTRecord");
            }
            if (remaining == LittleEndianConsts.SHORT_SIZE)
            {
                setExpectedChars(LittleEndian.getShort(data, offset));
                _unfinished_string = "";
                break;
            }
            short char_count = LittleEndian.getShort(data, offset);

            setupStringParameters(data, offset, char_count);
            if (remaining < _total_length_bytes)
            {
                setExpectedChars(calculateCharCount(_total_length_bytes
                                                    - remaining));
                char_count          -= getExpectedChars();
                _total_length_bytes = remaining;
            }
            else
            {
                setExpectedChars(0);
            }
            processString(data, offset, char_count);
            offset += _total_length_bytes;
            if (getExpectedChars() != 0)
            {
                break;
            }
        }
    }

    private void setupStringParameters(final byte [] data, final int index,
                                       final int char_count)
    {
        byte flag = data[ index + LittleEndianConsts.SHORT_SIZE ];

        _wide_char = (flag & 1) == 1;
        boolean extended      = (flag & 4) == 4;
        boolean formatted_run = (flag & 8) == 8;

        _total_length_bytes = _string_minimal_overhead
                              + calculateByteCount(char_count);
        _string_data_offset = _string_minimal_overhead;
        if (formatted_run)
        {
            short run_count = LittleEndian.getShort(data,
                                                    index
                                                    + _string_data_offset);

            _string_data_offset += LittleEndianConsts.SHORT_SIZE;
            _total_length_bytes += LittleEndianConsts.SHORT_SIZE
                                   + (LittleEndianConsts.INT_SIZE
                                      * run_count);
        }
        if (extended)
        {
            int extension_length = LittleEndian.getInt(data,
                                                       index
                                                       + _string_data_offset);

            _string_data_offset += LittleEndianConsts.INT_SIZE;
            _total_length_bytes += LittleEndianConsts.INT_SIZE
                                   + extension_length;
        }
    }

    private void processString(final byte [] data, final int index,
                               final short char_count)
    {
        byte[] str_data = new byte[ _total_length_bytes ];
        int    length   = _string_minimal_overhead
                          + calculateByteCount(char_count);
        byte[] bstring  = new byte[ length ];

        System.arraycopy(data, index, str_data, 0, str_data.length);
        int offset = 0;

        LittleEndian.putShort(bstring, offset, char_count);
        offset            += LittleEndianConsts.SHORT_SIZE;
        bstring[ offset ] = str_data[ offset ];
        System.arraycopy(str_data, _string_data_offset, bstring,
                         _string_minimal_overhead,
                         bstring.length - _string_minimal_overhead);
        UnicodeString string = new UnicodeString(UnicodeString.sid,
                                                 ( short ) bstring.length,
                                                 bstring);

        if (getExpectedChars() != 0)
        {
            _unfinished_string = string.getString();
        }
        else
        {
            Integer integer = new Integer(field_3_strings.size());

            field_3_strings.put(integer, string);
        }
    }

    private void setExpectedChars(final int count)
    {
        __expected_chars = count;
    }

    private int calculateByteCount(final int character_count)
    {
        return character_count * (_wide_char ? LittleEndianConsts.SHORT_SIZE
                                             : LittleEndianConsts.BYTE_SIZE);
    }

    private int calculateCharCount(final int byte_count)
    {
        return byte_count / (_wide_char ? LittleEndianConsts.SHORT_SIZE
                                        : LittleEndianConsts.BYTE_SIZE);
    }

    public int getRecordSize()
    {
        _record_lengths = new ArrayList();
        int retval      = 0;
        int unicodesize = calculateUnicodeSize();

        if (unicodesize > _max_data_space)
        {
            UnicodeString unistr             = null;
            int           stringreminant     = 0;
            int           unipos             = 0;
            boolean       lastneedcontinue   = false;
            int           stringbyteswritten = 0;
            boolean       finished           = false;
            boolean       first_record       = true;
            int           totalWritten       = 0;

            while (!finished)
            {
                int record = 0;
                int pos    = 0;

                if (first_record)
                {

                    record       = _max;
                    pos          = 12;
                    first_record = false;
                    _record_lengths.add(new Integer(record
                                                    - _std_record_overhead));
                }
                else
                {

                    pos = 0;
                    int to_be_written = (unicodesize - stringbyteswritten)
                                        + (lastneedcontinue ? 1
                                                            : 0);
                    int size          = Math.min(_max - _std_record_overhead,
                                                 to_be_written);

                    if (size == to_be_written)
                    {
                        finished = true;
                    }
                    record = size + _std_record_overhead;
                    _record_lengths.add(new Integer(size));
                    pos = 4;
                }
                if (lastneedcontinue)
                {
                    int available = _max - pos;

                    if (stringreminant <= available)
                    {

                        stringbyteswritten += stringreminant - 1;
                        pos                += stringreminant;
                        lastneedcontinue   = false;
                    }
                    else
                    {

                        int toBeWritten = unistr.maxBrokenLength(available);

                        if (available != toBeWritten)
                        {
                            int shortrecord = record
                                              - (available - toBeWritten);

                            _record_lengths.set(
                                _record_lengths.size() - 1,
                                new Integer(
                                    shortrecord - _std_record_overhead));
                            record = shortrecord;
                        }
                        stringbyteswritten += toBeWritten - 1;
                        pos                += toBeWritten;
                        stringreminant     -= toBeWritten - 1;
                        lastneedcontinue   = true;
                    }
                }
                for (; unipos < field_3_strings.size(); unipos++)
                {
                    int     available = _max - pos;
                    Integer intunipos = new Integer(unipos);

                    unistr =
                        (( UnicodeString ) field_3_strings.get(intunipos));
                    if (unistr.getRecordSize() <= available)
                    {
                        stringbyteswritten += unistr.getRecordSize();
                        pos                += unistr.getRecordSize();
                    }
                    else
                    {
                        if (available >= _string_minimal_overhead)
                        {
                            int toBeWritten =
                                unistr.maxBrokenLength(available);

                            stringbyteswritten += toBeWritten;
                            stringreminant     =
                                (unistr.getRecordSize() - toBeWritten)
                                + LittleEndianConsts.BYTE_SIZE;
                            if (available != toBeWritten)
                            {
                                int shortrecord = record
                                                  - (available - toBeWritten);

                                _record_lengths.set(
                                    _record_lengths.size() - 1,
                                    new Integer(
                                        shortrecord - _std_record_overhead));
                                record = shortrecord;
                            }
                            lastneedcontinue = true;
                            unipos++;
                        }
                        else
                        {
                            int shortrecord = record - available;

                            _record_lengths.set(
                                _record_lengths.size() - 1,
                                new Integer(
                                    shortrecord - _std_record_overhead));
                            record = shortrecord;
                        }
                        break;
                    }
                }
                totalWritten += record;
            }
            retval = totalWritten;
        }
        else
        {

            retval = _sst_record_overhead + unicodesize;
            _record_lengths.add(new Integer(unicodesize));
        }
        return retval;
    }

    private int calculateUnicodeSize()
    {
        int retval = 0;

        for (int k = 0; k < field_3_strings.size(); k++)
        {
            UnicodeString string =
                ( UnicodeString ) field_3_strings.get(new Integer(k));

            retval += string.getRecordSize();
        }
        return retval;
    }
}
