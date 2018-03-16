package org.apache.camel.component.file;

import java.io.File;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.impl.DefaultExchange;

/**
 * A {@link Exchange} for File
 *
 * @version $Revision: 647463 $
 */
public class FileExchange extends DefaultExchange {
    private File file;

    public FileExchange(CamelContext camelContext, ExchangePattern pattern, File file) {
        super(camelContext, pattern);
        setIn(new FileMessage(file));
        this.file = file;
    }

    public FileExchange(DefaultExchange parent, File file) {
        super(parent);
        this.file = file;
    }

    public File getFile() {
        return this.file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Exchange newInstance() {
        return new FileExchange(this, getFile());
    }

}
