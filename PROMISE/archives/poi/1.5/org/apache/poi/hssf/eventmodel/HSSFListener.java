package org.apache.poi.hssf.eventmodel;

import org.apache.poi.hssf.record.Record;

/**
 * Interface for use with the HSSFRequest and HSSFEventFactory.  Users should create
 * a listener supporting this interface and register it with the HSSFRequest (associating
 * it with Record SID's).
 *
 * @see org.apache.poi.hssf.eventmodel.HSSFEventFactory
 * @see org.apache.poi.hssf.eventmodel.HSSFRequest
 * @author  andy
 */

public interface HSSFListener
{

    /**
     * process an HSSF Record. Called when a record occurs in an HSSF file.
     */

    public void processRecord(Record record);
}
