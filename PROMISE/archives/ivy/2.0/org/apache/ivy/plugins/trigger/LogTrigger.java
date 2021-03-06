package org.apache.ivy.plugins.trigger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.ivy.core.IvyPatternHelper;
import org.apache.ivy.core.event.IvyEvent;
import org.apache.ivy.core.resolve.ResolveProcessException;
import org.apache.ivy.util.Message;

/**
 * A trigger performing logging.
 * <p>
 * The implementation is widely inspired by Ant Echo task.
 * </p>
 */
public class LogTrigger extends AbstractTrigger {
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private String message = "";
    
    private File file = null;

    private boolean append = true;
    /** encoding; set to null or empty means 'default' */
    private String encoding = "";

    public void progress(IvyEvent event) {
        log(IvyPatternHelper.substituteVariables(message, event.getAttributes()));
    }

    /**
     * Logs the given message.
     * 
     * @param message the message to log
     */
    protected void log(String message) {
        if (file == null) {
            Message.info(message);
        } else {
            Writer out = null;
            try {
                message += LINE_SEPARATOR;
                String filename = file.getAbsolutePath();
                if (encoding == null || encoding.length() == 0) {
                    out = new FileWriter(filename, append);
                } else {
                    out = new BufferedWriter(
                            new OutputStreamWriter(
                                new FileOutputStream(filename, append), encoding));
                }
                out.write(message, 0, message.length());
            } catch (IOException e) {
                throw new ResolveProcessException(e);
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        throw new ResolveProcessException(e);
                    }
                }
            }
        }
    }

    /**
     * Message to write.
     *
     * @param msg Sets the value for the message variable.
     */
    public void setMessage(String msg) {
        this.message = msg;
    }

    /**
     * File to write to.
     * @param file the file to write to, if not set, echo to standard Ivy logging
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * If true, append to existing file.
     * @param append if true, append to existing file, default is false.
     */
    public void setAppend(boolean append) {
        this.append = append;
    }

    /**
     * Declare the encoding to use when outputting to a file;
     * Use "" for the platform's default encoding.
     * @param encoding the character encoding to use.
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
}
