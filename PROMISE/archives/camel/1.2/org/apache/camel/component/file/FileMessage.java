package org.apache.camel.component.file;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultMessage;

import java.io.File;

/**
 * A {@link Exchange} for File
 * 
 * @version $Revision: 520985 $
 */
public class FileMessage extends DefaultMessage {
    private File file;

    public FileMessage() {
        this(new File("."));
    }

    public FileMessage(File file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "FileMessage: " + file;
    }

    @Override
    public FileExchange getExchange() {
        return (FileExchange)super.getExchange();
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public FileMessage newInstance() {
        return new FileMessage();
    }

    @Override
    protected Object createBody() {
        return file;
    }
}
