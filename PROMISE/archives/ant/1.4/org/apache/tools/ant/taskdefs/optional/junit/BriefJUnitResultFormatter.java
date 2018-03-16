package org.apache.tools.ant.taskdefs.optional.junit;

import org.apache.tools.ant.BuildException;

import junit.framework.*;

/**
 * Prints plain text output of the test to a specified Writer.
 * Inspired by the PlainJUnitResultFormatter.
 *
 * @author <a href="mailto:robertdw@bigpond.net.au">Robert Watkins</a>
 *
 * @see FormatterElement
 * @see PlainJUnitResultFormatter
 */
public class BriefJUnitResultFormatter implements JUnitResultFormatter {
        
    /**
     * Where to write the log to.
     */
    private java.io.OutputStream m_out;

    /**
     * Used for writing the results.
     */
    private java.io.PrintWriter m_output;
    
    /**
     * Used as part of formatting the results.
     */
    private java.io.StringWriter m_results;

    /**
     * Used for writing formatted results to.
     */
    private java.io.PrintWriter m_resultWriter;
    
    /**
     * Formatter for timings.
     */
    private java.text.NumberFormat m_numberFormat = java.text.NumberFormat.getInstance();
    
    /**
     * Output suite has written to System.out
     */
    private String systemOutput = null;

    /**
     * Output suite has written to System.err
     */
    private String systemError = null;

    public BriefJUnitResultFormatter() {
        m_results = new java.io.StringWriter();
        m_resultWriter = new java.io.PrintWriter(m_results);
    }
    
    /**
     * Sets the stream the formatter is supposed to write its results to.
     */
    public void setOutput(java.io.OutputStream out) { 
        m_out = out; 
        m_output = new java.io.PrintWriter(out); 
    }
    
    public void setSystemOutput(String out) {
        systemOutput = out;
    }

    public void setSystemError(String err) {
        systemError = err;
    }

    protected java.io.PrintWriter output() { return m_output; }
    
    protected java.io.PrintWriter resultWriter() { return m_resultWriter; }
    
    /**
     * The whole testsuite started.
     */
    public void startTestSuite(JUnitTest suite) throws BuildException {
    }

    /**
     * The whole testsuite ended.
     */
    public void endTestSuite(JUnitTest suite) throws BuildException {
        String newLine = System.getProperty("line.separator");
        StringBuffer sb = new StringBuffer("Testsuite: ");
        sb.append(suite.getName());
        sb.append(newLine);
        sb.append("Tests run: ");
        sb.append(suite.runCount());
        sb.append(", Failures: ");
        sb.append(suite.failureCount());
        sb.append(", Errors: ");
        sb.append(suite.errorCount());
        sb.append(", Time elapsed: ");
        sb.append(m_numberFormat.format(suite.getRunTime()/1000.0));
        sb.append(" sec");
        sb.append(newLine);
        sb.append(newLine);

        if (systemOutput != null && systemOutput.length() > 0) {
            sb.append("------------- Standard Output ---------------" )
                .append(newLine)
                .append(systemOutput)
                .append("------------- ---------------- ---------------" )
                .append(newLine);
        }
        
        if (systemError != null && systemError.length() > 0) {
            sb.append("------------- Standard Error -----------------" )
                .append(newLine)
                .append(systemError)
                .append("------------- ---------------- ---------------" )
                .append(newLine);
        }

        if ( output() != null) {
            try {
                output().write(sb.toString());
                resultWriter().close();
                output().write(m_results.toString());
                output().flush();
            } finally {
                if (m_out != (Object)System.out &&
                    m_out != (Object)System.err) {
                    try {
                        m_out.close();
                    } catch (java.io.IOException e) {}
                }
            }
        }
    }
    
    /**
     * A test started.
     */
    public void startTest(Test test) {
    }
    
    /**
     * A test ended.
     */
    public void endTest(Test test) {
    }

    /**
     * Interface TestListener for JUnit &lt;= 3.4.
     *
     * <p>A Test failed.
     */
    public void addFailure(Test test, Throwable t) {
        formatError("\tFAILED", test, t);
    }

    /**
     * Interface TestListener for JUnit &gt; 3.4.
     *
     * <p>A Test failed.
     */
    public void addFailure(Test test, AssertionFailedError t) {
        addFailure(test, (Throwable) t);
    }

    /**
     * A test caused an error.
     */
    public void addError(Test test, Throwable error) {
        formatError("\tCaused an ERROR", test, error);
    }
    
    /**
     * Format the test for printing..
     */
    protected String formatTest(Test test) {
        if (test == null) {
            return "Null Test: ";
        }
        else {
            return "Testcase: " + test.toString() + ":";
        }
    }

    /**
     * Format an error and print it.
     */
    protected void formatError(String type, Test test, Throwable error) {
        if (test != null) {
            endTest(test);
        }

        resultWriter().println(formatTest(test) + ":" + type);
        resultWriter().println(error.getMessage());
        error.printStackTrace(resultWriter());
        resultWriter().println("");
    }
}
