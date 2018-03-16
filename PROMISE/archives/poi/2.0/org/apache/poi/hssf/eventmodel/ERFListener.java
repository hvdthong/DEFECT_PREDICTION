package org.apache.poi.hssf.eventmodel;

import org.apache.poi.hssf.record.Record;

/**
 * An ERFListener is registered with the EventRecordFactory.
 * An ERFListener listens for Records coming from the stream
 * via the EventRecordFactory
 * 
 * @see EventRecordFactory
 * @author Andrew C. Oliver acoliver@apache.org
 */
public interface ERFListener
{
    /**
     * Process a Record.  This method is called by the 
     * EventRecordFactory when a record is returned.
     * @return boolean specifying whether the effort was a success.
     */
    public boolean processRecord(Record rec);
}
