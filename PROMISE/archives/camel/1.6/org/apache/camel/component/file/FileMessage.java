package org.apache.camel.component.file;

import java.io.File;

import org.apache.camel.impl.DefaultMessage;

/**
 * A {@link org.apache.camel.Message Message} for File
 *
 * @version $Revision: 659782 $
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
