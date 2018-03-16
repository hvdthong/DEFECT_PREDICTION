package org.apache.poi.hssf.dev;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;

import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.hssf.eventmodel.*;
import org.apache.poi.hssf.record.Record;

/**
 *
 * @author  andy
 */

public class EFBiffViewer
{
    String file;

    /** Creates a new instance of EFBiffViewer */

    public EFBiffViewer()
    {
    }

    public void run()
        throws IOException
    {
        FileInputStream fin   = new FileInputStream(file);
        POIFSFileSystem poifs = new POIFSFileSystem(fin);
        InputStream     din   = poifs.createDocumentInputStream("Workbook");
        HSSFRequest     req   = new HSSFRequest();

        req.addListenerForAllRecords(new HSSFListener()
        {
            public void processRecord(Record rec)
            {
                System.out.println(rec.toString());
            }
        });
        HSSFEventFactory factory = new HSSFEventFactory();

        factory.processEvents(req, din);
    }

    public void setFile(String file)
    {
        this.file = file;
    }

    public static void main(String [] args)
    {
        if ((args.length == 1) && !args[ 0 ].equals("--help"))
        {
            try
            {
                EFBiffViewer viewer = new EFBiffViewer();

                viewer.setFile(args[ 0 ]);
                viewer.run();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            System.out.println("EFBiffViewer");
            System.out.println(
                "Outputs biffview of records based on HSSFEventFactory");
            System.out
                .println("usage: java org.apache.poi.hssf.dev.EBBiffViewer "
                         + "filename");
        }
    }
}
