package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.*;
import java.io.*;

/**
 * Serves as an output stream to Javac. This let's us print messages
 * out to the log and detect whether or not Javac had an error
 * while compiling.
 *
 * @author James Duncan Davidson (duncan@x180.com)
 * @deprecated use returnvalue of compile to detect compilation failure.
 */

class JavacOutputStream extends OutputStream {

    private Task task;
    private StringBuffer line;
    private boolean errorFlag = false;

    /**
     * Constructs a new JavacOutputStream with the given task
     * as the output source for messages.
     */
    
    JavacOutputStream(Task task) {
        this.task = task;
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
        if (s.indexOf("error") > -1) {
            errorFlag = true;
        }
        task.log(s);
        line = new StringBuffer();
    }

    /**
     * Returns the error status of the compile. If no errors occured,
     * this method will return false, else this method will return true.
     */
    
    boolean getErrorFlag() {
        return errorFlag;
    }
}

