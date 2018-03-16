package org.apache.tools.ant.taskdefs.optional.junit;

import java.io.OutputStream;
import org.apache.tools.ant.BuildException;
import junit.framework.TestListener;

/**
 * This Interface describes classes that format the results of a JUnit
 * testrun.
 * 
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 */
public interface JUnitResultFormatter extends TestListener {
    /**
     * The whole testsuite started.
     */
    void startTestSuite(JUnitTest suite) throws BuildException;
    
    /**
     * The whole testsuite ended.
     */
    void endTestSuite(JUnitTest suite) throws BuildException;

    /**
     * Sets the stream the formatter is supposed to write its results to.
     */
    void setOutput(OutputStream out);

    /**
     * This is what the test has written to System.out
     */
    void setSystemOutput(String out);

    /**
     * This is what the test has written to System.err
     */
    void setSystemError(String err);
}
