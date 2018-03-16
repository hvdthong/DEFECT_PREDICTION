package org.apache.poi.hssf.eventmodel;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.model.Model;
import org.apache.poi.hssf.model.Sheet;
import org.apache.poi.hssf.model.Workbook;
import org.apache.poi.hssf.record.BOFRecord;
import org.apache.poi.hssf.record.EOFRecord;
import org.apache.poi.hssf.record.Record;


/**
 * ModelFactory creates workbook and sheet models based upon 
 * events thrown by them there events from the EventRecordFactory.
 * 
 * @see org.apache.poi.hssf.eventmodel.EventRecordFactory
 * @author Andrew C. Oliver acoliver@apache.org
 */
public class ModelFactory implements ERFListener
{

    List listeners;
    Model currentmodel;
    boolean lastEOF;
 
    /**
     * Constructor for ModelFactory.  Does practically nothing.
     */
    public ModelFactory()
    {
        super();
        listeners = new ArrayList(1);
    }
    
    /**
     * register a ModelFactoryListener so that it can receive 
     * Models as they are created.
     */
    public void registerListener(ModelFactoryListener listener) {
        listeners.add(listener);
    }
    
    /**
     * Start processing the Workbook stream into Model events.
     */
    public void run(InputStream stream) {
        EventRecordFactory factory = new EventRecordFactory(true);
        factory.registerListener(this,null);
        lastEOF = true;
        factory.processRecords(stream);
    }

    public boolean processRecord(Record rec)
    {
       if (rec.getSid() == BOFRecord.sid) {
             if (lastEOF != true) {
              throw new RuntimeException("Not yet handled embedded models");  
             } else {
              BOFRecord bof = (BOFRecord)rec;
              switch (bof.getType()) {
               case BOFRecord.TYPE_WORKBOOK:
                 currentmodel = new Workbook();                 
               break;
               case BOFRecord.TYPE_WORKSHEET:
                 currentmodel = new Sheet();                                  
               break;
              default:
                   throw new RuntimeException("Unsupported model type "+bof.getType());
              }                
               
             }        
        }
        
        if (rec.getSid() == EOFRecord.sid) {
            lastEOF = true;
            throwEvent(currentmodel);
        } else {
            lastEOF = false;   
        }
        
 
        return true;
    }

    /**
     * Throws the model as an event to the listeners
     * @param model to be thrown
     */
    private void throwEvent(Model model)
    {
        Iterator i = listeners.iterator();
        while (i.hasNext()) {
          ModelFactoryListener mfl = (ModelFactoryListener) i.next();
          mfl.process(model);
        }
    }


}
