package org.apache.camel.component.file;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.impl.DefaultExchange;

/**
 * A {@link Exchange} for File
 *
 * @version $Revision: 723860 $
 */
public class FileExchange extends DefaultExchange {
    private File file;

    public FileExchange(CamelContext camelContext, ExchangePattern pattern, File file) {
        super(camelContext, pattern);
        setFile(file);
    }

    public FileExchange(DefaultExchange parent, File file) {
        super(parent);
        setFile(file);
    }

    public File getFile() {
        return this.file;
    }

    public void setFile(File file) {
        setIn(new FileMessage(file));
        this.file = file;
        populateHeaders(file);
    }

    public Exchange newInstance() {
        return new FileExchange(this, getFile());
    }

    private void populateHeaders(File file) {
        if (file != null) {
            getIn().setHeader("CamelFileName", file.getName());
            getIn().setHeader("CamelFileAbsolutePath", file.getAbsolutePath());
            getIn().setHeader("CamelFileParent", file.getParent());
            getIn().setHeader("CamelFilePath", file.getPath());
            try {
                getIn().setHeader("CamelFileCanonicalPath", file.getCanonicalPath());
            } catch (IOException e) {
            }
            if (file.length() > 0) {
                getIn().setHeader("CamelFileLength", new Long(file.length()));
            }
            if (file.lastModified() > 0) {
                getIn().setHeader("CamelFileLastModified", new Date(file.lastModified()));
            }
        }
    }

}
