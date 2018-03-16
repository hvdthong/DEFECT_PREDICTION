package org.apache.tools.ant.taskdefs.optional.junit;

import org.apache.tools.ant.BuildException;
import junit.framework.TestListener;

/**
 * This Interface describes classes that format the results of a JUnit
 * testrun.
 * 
 * @author <a href="mailto:stefan.bodewig@megabit.net">Stefan Bodewig</a>
 */
public interface JUnitResultFormatter extends TestListener {
    /**
     * The whole testsuite started.
     */
    public void startTestSuite(JUnitTest suite) throws BuildException;

    /**
     * The whole testsuite ended.
     */
    public void endTestSuite(JUnitTest suite) throws BuildException;

    /**
     * Sets the stream the formatter is supposed to write its results to.
     */
    public void setOutput(java.io.OutputStream out);
}
