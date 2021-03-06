package org.apache.poi.hssf.eventmodel;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.RecordFactory;

/**
 * An HSSFRequest object should be constructed registering an instance or multiple
 * instances of HSSFListener with each Record.sid you wish to listen for.
 *
 * @see org.apache.poi.hssf.eventmodel.HSSFEventFactory
 * @see org.apache.poi.hssf.eventmodel.HSSFListener
 * @see org.apache.poi.hssf.dev.EFHSSF
 * @author  andy
 */

public class HSSFRequest
{
    private HashMap records;

    /** Creates a new instance of HSSFRequest */

    public HSSFRequest()
    {
        records =
    }

    /**
     * add an event listener for a particular record type.  The trick is you have to know
     * what the records are for or just start with our examples and build on them.  Alternatively,
     * you CAN call addListenerForAllRecords and you'll recieve ALL record events in one listener,
     * but if you like to squeeze every last byte of efficiency out of life you my not like this.
     * (its sure as heck what I plan to do)
     *
     * @see #addListenerForAllRecords(HSSFListener)
     *
     * @param lsnr      for the event
     * @param sid       identifier for the record type this is the .sid static member on the individual records
     *        for example req.addListener(myListener, BOFRecord.sid)
     */

    public void addListener(HSSFListener lsnr, short sid)
    {
        List   list = null;
        Object obj  = records.get(new Short(sid));

        if (obj != null)
        {
            list = ( List ) obj;
        }
        else
        {
            list = new ArrayList(
            list.add(lsnr);
            records.put(new Short(sid), list);
        }
    }

    /**
     * This is the equivilent of calling addListener(myListener, sid) for EVERY
     * record in the org.apache.poi.hssf.record package. This is for lazy
     * people like me. You can call this more than once with more than one listener, but
     * that seems like a bad thing to do from a practice-perspective unless you have a
     * compelling reason to do so (like maybe you send the event two places or log it or
     * something?).
     *
     * @param lsnr      a single listener to associate with ALL records
     */

    public void addListenerForAllRecords(HSSFListener lsnr)
    {
        short[] rectypes = RecordFactory.getAllKnownRecordSIDs();

        for (int k = 0; k < rectypes.length; k++)
        {
            addListener(lsnr, rectypes[ k ]);
        }
    }

    /**
     * called by HSSFEventFactory, passes the Record to each listener associated with
     * a record.sid.
     */

    protected void processRecord(Record rec)
    {
        Object obj = records.get(new Short(rec.getSid()));

        if (obj != null)
        {
            List listeners = ( List ) obj;

            for (int k = 0; k < listeners.size(); k++)
            {
                HSSFListener listener = ( HSSFListener ) listeners.get(k);

                listener.processRecord(rec);
            }
        }
    }
}
