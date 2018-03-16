package org.apache.poi.hssf.eventmodel;

import java.io.InputStream;
import java.io.IOException;

import org.apache.poi.util.LittleEndian;
import org.apache.poi.hssf.record.RecordFormatException;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.RecordFactory;
import org.apache.poi.hssf.record.ContinueRecord;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * Low level event based HSSF reader.  Pass either a DocumentInputStream to
 * process events along with a request object or pass a POIFS POIFSFileSystem to
 * processWorkbookEvents along with a request.
 *
 * This will cause your file to be processed a record at a time.  Each record with
 * a static id matching one that you have registed in your HSSFRequest will be passed
 * to your associated HSSFListener.
 *
 * @see org.apache.poi.hssf.dev.EFHSSF
 *
 * @author  andy
 */

public class HSSFEventFactory
{

    /** Creates a new instance of HSSFEventFactory */

    public HSSFEventFactory()
    {
    }

    /**
     * Processes a file into essentially record events.
     *
     * @param req       an Instance of HSSFRequest which has your registered listeners
     * @param fs        a POIFS filesystem containing your workbook
     */

    public void processWorkbookEvents(HSSFRequest req, POIFSFileSystem fs)
        throws IOException
    {
        InputStream in = fs.createDocumentInputStream("Workbook");

        processEvents(req, in);
    }

    /**
     * Processes a DocumentInputStream into essentially Record events.
     *
     * @see org.apache.poi.poifs.filesystem.POIFSFileSystem#createDocumentInputStream(String)
     * @param req       an Instance of HSSFRequest which has your registered listeners
     * @param in        a DocumentInputStream obtained from POIFS's POIFSFileSystem object
     */

    public void processEvents(HSSFRequest req, InputStream in)
        throws IOException
    {
        try
        {
            byte[] sidbytes  = new byte[ 2 ];
            int    bytesread = in.read(sidbytes);
            Record rec       = null;

            while (bytesread > 0)
            {
                short sid = 0;

                sid = LittleEndian.getShort(sidbytes);
                if ((rec != null) && (sid != ContinueRecord.sid))
                {
                    req.processRecord(rec);
                }
                if (sid != ContinueRecord.sid)
                {
                    short  size = LittleEndian.readShort(in);
                    byte[] data = new byte[ size ];

                    if (data.length > 0)
                    {
                        in.read(data);
                    }
                    Record[] recs = RecordFactory.createRecord(sid, size,
                                                               data);

                    if (recs.length > 1)
                        for (int k = 0; k < (recs.length - 1); k++)
                            req.processRecord(
                        }
                    }

                }
                else
                    short  size = LittleEndian.readShort(in);
                    byte[] data = new byte[ size ];

                    if (data.length > 0)
                    {
                        in.read(data);
                    }
                    rec.processContinueRecord(data);
                }
            }
            if (rec != null)
            {
                req.processRecord(rec);
            }
        }
        catch (IOException e)
        {
            throw new RecordFormatException("Error reading bytes");
        }

    }
}
