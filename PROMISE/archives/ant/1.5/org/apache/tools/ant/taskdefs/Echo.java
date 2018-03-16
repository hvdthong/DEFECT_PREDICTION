package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.EnumeratedAttribute;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
/**
 * Writes a message to the Ant logging facilities.
 *
 * @author costin@dnt.ro
 *
 * @since Ant 1.1
 *
 * @ant.task category="utility"
 */
public class Echo extends Task {
    protected File file = null;
    protected boolean append = false;
    
    protected int logLevel = Project.MSG_WARN;   
    
    /**
     * Does the work.
     *
     * @exception BuildException if someting goes wrong with the build
     */
    public void execute() throws BuildException {
        if (file == null) {
            log(message, logLevel);
        } else {
            FileWriter out = null;
            try {
                out = new FileWriter(file.getAbsolutePath(), append);
                out.write(message, 0, message.length());
            } catch (IOException ioe) {
                throw new BuildException(ioe, location);
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException ioex) {}
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
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * If true, append to existing file.
     */
    public void setAppend(boolean append) {
        this.append = append;
    }

    /**
     * Set a multiline message.
     */
    public void addText(String msg) {
        message += project.replaceProperties(msg);
    }

    /**
     * Set the logging level. Level should be one of
     * <ul>
     *  <li>error</li>
     *  <li>warning</li>
     *  <li>info</li>
     *  <li>verbose</li>
     *  <li>debug</li>
     * </ul>
     * <p>The default is &quot;warning&quot; to ensure that messages are
     * displayed by default when using the -quiet command line option.</p>
     */
    public void setLevel(EchoLevel echoLevel) {
        String option = echoLevel.getValue();
        if (option.equals("error")) {
            logLevel = Project.MSG_ERR;
        } else if (option.equals("warning")) {
            logLevel = Project.MSG_WARN;
        } else if (option.equals("info")) {
            logLevel = Project.MSG_INFO;
        } else if (option.equals("verbose")) {
            logLevel = Project.MSG_VERBOSE;
        } else {
            logLevel = Project.MSG_DEBUG;
        }
    }

    public static class EchoLevel extends EnumeratedAttribute {
        /**
         * @see EnumeratedAttribute#getValues
         */
        public String[] getValues() {
            return new String[] {"error", "warning", "info", 
                                 "verbose", "debug"};
        }
    }
}
