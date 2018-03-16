package org.apache.tools.ant.taskdefs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

/**
 * Parses output from jikes and
 * passes errors and warnings
 * into the right logging channels of Project.
 *
 * <p><strong>As of Ant 1.2, this class is considered to be dead code
 * by the Ant developers and is unmaintained.  Don't use
 * it.</strong></p>
 *
 * @deprecated since 1.2.
 *             Use Jikes' exit value to detect compilation failure.
 */
public class JikesOutputParser implements ExecuteStreamHandler {
    protected Task task;
    protected int errors;
    protected int warnings;
    protected boolean error = false;
    protected boolean emacsMode;

    protected BufferedReader br;

    /**
     * Ignore.
     * @param os ignored
     */
    public void setProcessInputStream(OutputStream os) {
    }

    /**
     * Ignore.
     * @param is ignored
     */
    public void setProcessErrorStream(InputStream is) {
    }

    /**
     * Set the inputstream
     * @param is the input stream
     * @throws IOException on error
     */
    public void setProcessOutputStream(InputStream is) throws IOException {
        br = new BufferedReader(new InputStreamReader(is));
    }

    /**
     * Invokes parseOutput.
     * @throws IOException on error
     */
    public void start() throws IOException {
        parseOutput(br);
    }

    /**
     * Ignore.
     */
    public void stop() {
    }

    /**
     * Construct a new Parser object
     * @param task      task in which context we are called
     * @param emacsMode if true output in emacs mode
     */
    protected JikesOutputParser(Task task, boolean emacsMode) {
        super();

        System.err.println("As of Ant 1.2 released in October 2000, the "
            + "JikesOutputParser class");
        System.err.println("is considered to be dead code by the Ant "
            + "developers and is unmaintained.");
        System.err.println("Don\'t use it!");

        this.task = task;
        this.emacsMode = emacsMode;
    }

    /**
     * Parse the output of a jikes compiler
     * @param reader - Reader used to read jikes's output
     * @throws IOException on error
     */
    protected void parseOutput(BufferedReader reader) throws IOException {
       if (emacsMode) {
           parseEmacsOutput(reader);
       } else {
           parseStandardOutput(reader);
       }
    }

    private void parseStandardOutput(BufferedReader reader) throws IOException {
        String line;
        String lower;


        while ((line = reader.readLine()) != null) {
            lower = line.toLowerCase();
            if (line.trim().equals("")) {
                continue;
            }
            if (lower.indexOf("error") != -1) {
                setError(true);
            } else if (lower.indexOf("warning") != -1) {
                setError(false);
                   } else {
                if (emacsMode) {
                    setError(true);
                }
            }
            log(line);
        }
    }

    private void parseEmacsOutput(BufferedReader reader) throws IOException {
       parseStandardOutput(reader);
    }

    private void setError(boolean err) {
        error = err;
        if (error) {
            errorFlag = true;
        }
    }

    private void log(String line) {
       if (!emacsMode) {
           task.log("", (error ? Project.MSG_ERR : Project.MSG_WARN));
       }
       task.log(line, (error ? Project.MSG_ERR : Project.MSG_WARN));
    }

    /**
     * Indicate if there were errors during the compile
     * @return if errors occurred
     */
    protected boolean getErrorFlag() {
        return errorFlag;
    }
}
