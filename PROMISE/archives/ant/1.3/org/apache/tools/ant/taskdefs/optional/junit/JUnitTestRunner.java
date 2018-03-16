package org.apache.tools.ant.taskdefs.optional.junit;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

import junit.framework.*;
import java.lang.reflect.*;
import java.io.*;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Simple Testrunner for JUnit that runs all tests of a testsuite.
 *
 * <p>This TestRunner expects a name of a TestCase class as its
 * argument. If this class provides a static suite() method it will be
 * called and the resulting Test will be run.
 *
 * <p>Otherwise all public methods starting with "test" and taking no
 * argument will be run.
 *
 * <p>Summary output is generated at the end.
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 */

public class JUnitTestRunner implements TestListener {

    /**
     * No problems with this test.
     */
    public static final int SUCCESS = 0;

    /**
     * Some tests failed.
     */
    public static final int FAILURES = 1;

    /**
     * An error occured.
     */
    public static final int ERRORS = 2;

    /**
     * Holds the registered formatters.
     */
    private Vector formatters = new Vector();

    /**
     * Collects TestResults.
     */
    private TestResult res;

    /**
     * Do we stop on errors.
     */
    private boolean haltOnError = false;

    /**
     * Do we stop on test failures.
     */
    private boolean haltOnFailure = false;

    /**
     * The corresponding testsuite.
     */
    private Test suite = null;

    /**
     * Exception caught in constructor.
     */
    private Exception exception;

    /**
     * Returncode
     */
    private int retCode = SUCCESS;

    /**
     * The TestSuite we are currently running.
     */
    private JUnitTest junitTest;

    /**
     * Constructor for fork=true or when the user hasn't specified a
     * classpath.  
     */
    public JUnitTestRunner(JUnitTest test, boolean haltOnError,
                           boolean haltOnFailure) {
        this(test, haltOnError, haltOnFailure, null);
    }

    /**
     * Constructor to use when the user has specified a classpath.
     */
    public JUnitTestRunner(JUnitTest test, boolean haltOnError,
                           boolean haltOnFailure, ClassLoader loader) {
        this.junitTest = test;
        this.haltOnError = haltOnError;
        this.haltOnFailure = haltOnFailure;

        try {
            Class testClass = null;
            if (loader == null) {
                testClass = Class.forName(test.getName());
            } else {
                testClass = loader.loadClass(test.getName());
            }
            
            Method suiteMethod = null;
            try {
                suiteMethod= testClass.getMethod("suite", new Class[0]);
            } catch(Exception e) {
            }
            if (suiteMethod != null){
                suite = (Test)suiteMethod.invoke(null, new Class[0]);
            } else {
                suite= new TestSuite(testClass);
            }
            
        } catch(Exception e) {
            retCode = ERRORS;
            exception = e;
        }
    }

    public void run() {
        res = new TestResult();
        res.addListener(this);
        for (int i=0; i < formatters.size(); i++) {
            res.addListener((TestListener)formatters.elementAt(i));
        }

        long start = System.currentTimeMillis();

        fireStartTestSuite();
            for (int i=0; i < formatters.size(); i++) {
                ((TestListener)formatters.elementAt(i)).addError(null, 
                                                                 exception);
            }
            junitTest.setCounts(1, 0, 1);
            junitTest.setRunTime(0);
        } else {
            suite.run(res);
            junitTest.setCounts(res.runCount(), res.failureCount(), 
                                res.errorCount());
            junitTest.setRunTime(System.currentTimeMillis() - start);
        }
        fireEndTestSuite();

        if (retCode != SUCCESS || res.errorCount() != 0) {
            retCode = ERRORS;
        } else if (res.failureCount() != 0) {
            retCode = FAILURES;
        }
    }

    /**
     * Returns what System.exit() would return in the standalone version.
     *
     * @return 2 if errors occurred, 1 if tests failed else 0.
     */
    public int getRetCode() {
        return retCode;
    }

    /**
     * Interface TestListener.
     *
     * <p>A new Test is started.
     */
    public void startTest(Test t) {}

    /**
     * Interface TestListener.
     *
     * <p>A Test is finished.
     */
    public void endTest(Test test) {}

    /**
     * Interface TestListener for JUnit &lt;= 3.4.
     *
     * <p>A Test failed.
     */
    public void addFailure(Test test, Throwable t) {
        if (haltOnFailure) {
            res.stop();
        }
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
     * Interface TestListener.
     *
     * <p>An error occured while running the test.
     */
    public void addError(Test test, Throwable t) {
        if (haltOnError) {
            res.stop();
        }
    }

    private void fireStartTestSuite() {
        for (int i=0; i<formatters.size(); i++) {
            ((JUnitResultFormatter)formatters.elementAt(i)).startTestSuite(junitTest);
        }
    }

    private void fireEndTestSuite() {
        for (int i=0; i<formatters.size(); i++) {
            ((JUnitResultFormatter)formatters.elementAt(i)).endTestSuite(junitTest);
        }
    }

    public void addFormatter(JUnitResultFormatter f) {
        formatters.addElement(f);
    }

    /**
     * Entry point for standalone (forked) mode.
     *
     * Parameters: testcaseclassname plus parameters in the format
     * key=value, none of which is required.
     *
     * <table cols="4" border="1">
     * <tr><th>key</th><th>description</th><th>default value</th></tr>
     *
     * <tr><td>haltOnError</td><td>halt test on
     * errors?</td><td>false</td></tr>
     *
     * <tr><td>haltOnFailure</td><td>halt test on
     * failures?</td><td>false</td></tr>
     *
     * <tr><td>formatter</td><td>A JUnitResultFormatter given as
     * classname,filename. If filename is ommitted, System.out is
     * assumed.</td><td>none</td></tr>
     *
     * </table> 
     */
    public static void main(String[] args) throws IOException {
        boolean exitAtEnd = true;
        boolean haltError = false;
        boolean haltFail = false;

        if (args.length == 0) {
            System.err.println("required argument TestClassName missing");
            System.exit(ERRORS);
        }

        for (int i=1; i<args.length; i++) {
            if (args[i].startsWith("haltOnError=")) {
                haltError = Project.toBoolean(args[i].substring(12));
            } else if (args[i].startsWith("haltOnFailure=")) {
                haltFail = Project.toBoolean(args[i].substring(14));
            } else if (args[i].startsWith("formatter=")) {
                try {
                    createAndStoreFormatter(args[i].substring(10));
                } catch (BuildException be) {
                    System.err.println(be.getMessage());
                    System.exit(ERRORS);
                }
            }
        }
        
        JUnitTest t = new JUnitTest(args[0]);
        JUnitTestRunner runner = new JUnitTestRunner(t, haltError, haltFail);
        transferFormatters(runner);
        runner.run();
        System.exit(runner.getRetCode());
    }

    private static Vector fromCmdLine = new Vector();

    private static void transferFormatters(JUnitTestRunner runner) {
        for (int i=0; i<fromCmdLine.size(); i++) {
            runner.addFormatter((JUnitResultFormatter) fromCmdLine.elementAt(i));
        }
    }

    /**
     * Line format is: formatter=<classname>(,<pathname>)?
     */
    private static void createAndStoreFormatter(String line)
        throws BuildException {
        FormatterElement fe = new FormatterElement();
        int pos = line.indexOf(',');
        if (pos == -1) {
            fe.setClassname(line);
        } else {
            fe.setClassname(line.substring(0, pos));
            fe.setOutfile( new File(line.substring(pos + 1)) );
        }
        fromCmdLine.addElement(fe.createFormatter());
    }

