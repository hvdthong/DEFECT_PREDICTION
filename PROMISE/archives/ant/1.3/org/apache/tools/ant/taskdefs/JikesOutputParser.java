package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.*;
    
import java.io.*;

/**
 * Parses output from jikes and
 * passes errors and warnings
 * into the right logging channels of Project.
 *
 * TODO: 
 * Parsing could be much better
 * @author skanthak@muehlheim.de
 * @deprecated use Jikes' exit value to detect compilation failure.
 */
public class JikesOutputParser implements ExecuteStreamHandler {
    protected Task task;
    protected int errors,warnings;
    protected boolean error = false;
    protected boolean emacsMode;
    
    protected BufferedReader br;

    /**
     * Ignore.
     */
    public void setProcessInputStream(OutputStream os) {}

    /**
     * Ignore.
     */
    public void setProcessErrorStream(InputStream is) {}

    /**
     * Set the inputstream
     */
    public void setProcessOutputStream(InputStream is) throws IOException {
        br = new BufferedReader(new InputStreamReader(is));
    }

    /**
     * Invokes parseOutput.
     */
    public void start() throws IOException {
        parseOutput(br);
    }

    /**
     * Ignore.
     */
    public void stop() {}

    /**
     * Construct a new Parser object
     * @param task - task in whichs context we are called
     */
    protected JikesOutputParser(Task task, boolean emacsMode) {
        super();
        this.task = task;
        this.emacsMode = emacsMode;
    }

    /**
     * Parse the output of a jikes compiler
     * @param reader - Reader used to read jikes's output
     */
    protected void parseOutput(BufferedReader reader) throws IOException {
       if (emacsMode)
           parseEmacsOutput(reader);
       else
           parseStandardOutput(reader);
    }

    private void parseStandardOutput(BufferedReader reader) throws IOException {
        String line;
        String lower;
        

        while ((line = reader.readLine()) != null) {
            lower = line.toLowerCase();
            if (line.trim().equals(""))
                continue;
            if (lower.indexOf("error") != -1)
                setError(true);
            else if (lower.indexOf("warning") != -1)
                setError(false);
            else {
                if (emacsMode)
                    setError(true);
            }
            log(line);
        }
    }

    private void parseEmacsOutput(BufferedReader reader) throws IOException {
       parseStandardOutput(reader);
    }

    private void setError(boolean err) {
        error = err;
        if(error)
            errorFlag = true;
    }

    private void log(String line) {
       if (!emacsMode) {
           task.log("", (error ? Project.MSG_ERR : Project.MSG_WARN));
       }
       task.log(line, (error ? Project.MSG_ERR : Project.MSG_WARN));
    }

    /**
     * Indicate if there were errors during the compile
     * @return if errors ocured
     */
    protected boolean getErrorFlag() {
        return errorFlag;
    }
}
