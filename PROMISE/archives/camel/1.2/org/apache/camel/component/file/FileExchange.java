package org.apache.camel.component.file;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.impl.DefaultExchange;

import java.io.File;

/**
 * A {@link Exchange} for File
 *
 * @version $Revision: 520985 $
 */
public class FileExchange extends DefaultExchange {
    private File file;

    public FileExchange(CamelContext camelContext, ExchangePattern pattern, File file) {
        super(camelContext, pattern);
        setIn(new FileMessage(file));
        this.file = file;
    }

    /**
     * @return the file
     */
    public File getFile() {
        return this.file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(File file) {
        this.file = file;
    }

    public Exchange newInstance() {
        return new FileExchange(getContext(), getPattern(), getFile());
    }
}
