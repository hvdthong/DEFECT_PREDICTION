package org.apache.poi.hssf.record;

/**
 * Title:     Record Format Exception
 * Description: Used by records to indicate invalid format/data.<P>
 */

public class RecordFormatException
    extends RuntimeException
{
    public RecordFormatException(String exception)
    {
        super(exception);
    }
}
