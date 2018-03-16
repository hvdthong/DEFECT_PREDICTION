package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.*;
import java.io.*;

/**
 * Redirects text written to a stream thru the standard
 * ant logging mechanism. This class is useful for integrating
 * with tools that write to System.out and System.err. For example,
 * the following will cause all text written to System.out to be
 * logged with "info" priority:
 * <pre>System.setOut(new PrintStream(new TaskOutputStream(project, Project.MSG_INFO)));</pre>
 *
 * @author James Duncan Davidson (duncan@x180.com)
 * @deprecated use LogOutputStream instead.
 */

public class TaskOutputStream extends OutputStream {

    private Task task;
    private StringBuffer line;
    private int msgOutputLevel;

    /**
     * Constructs a new JavacOutputStream with the given project
     * as the output source for messages.
     */

    TaskOutputStream(Task task, int msgOutputLevel) {
        this.task = task;
        this.msgOutputLevel = msgOutputLevel;

        line = new StringBuffer();
    }

    /**
     * Write a character to the output stream. This method looks
     * to make sure that there isn't an error being reported and
     * will flush each line of input out to the project's log stream.
     */

    public void write(int c) throws IOException {
        char cc = (char)c;
        if (cc == '\r' || cc == '\n') {
            if (line.length() > 0) {
                processLine();
            }
        } else {
            line.append(cc);
        }
    }

    /**
     * Processes a line of input and determines if an error occured.
     */

    private void processLine() {
        String s = line.toString();
        task.log(s, msgOutputLevel);
        line = new StringBuffer();
    }
}

