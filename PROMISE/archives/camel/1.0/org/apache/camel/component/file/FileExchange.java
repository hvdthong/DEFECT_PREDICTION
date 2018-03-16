package org.apache.camel.component.file;

import java.io.File;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultExchange;

/**
 * A {@link Exchange} for File
 * 
 * @version $Revision: 520985 $
 */
public class FileExchange extends DefaultExchange {

    private File file;
    /**
     * Constructor
     * @param camelContext
     * @param file
     */
    public FileExchange(CamelContext camelContext, File file) {
        super(camelContext);
        setIn(new FileMessage(file));
        this.file = file;
    }
    
    /**
     * @return the file
     */
    public File getFile(){
        return this.file;
    }
    
    /**
     * @param file the file to set
     */
    public void setFile(File file){
        this.file=file;
    }
}
