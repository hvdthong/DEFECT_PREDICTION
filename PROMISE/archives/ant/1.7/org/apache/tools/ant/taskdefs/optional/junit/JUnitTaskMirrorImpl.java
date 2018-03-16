package org.apache.tools.ant.taskdefs.optional.junit;

import java.io.OutputStream;
import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import junit.framework.TestResult;
import org.apache.tools.ant.AntClassLoader;

/**
 * Implementation of the part of the junit task which can directly refer to junit.* classes.
 * Public only to permit use of reflection; do not use directly.
 * @see JUnitTaskMirror
 * @see "bug #38799"
 * @since 1.7
 */
public final class JUnitTaskMirrorImpl implements JUnitTaskMirror {

    private final JUnitTask task;

    /**
     * Constructor.
     * @param task the junittask that uses this mirror.
     */
    public JUnitTaskMirrorImpl(JUnitTask task) {
        this.task = task;
    }

    /** {@inheritDoc}. */
    public void addVmExit(JUnitTest test, JUnitTaskMirror.JUnitResultFormatterMirror aFormatter,
            OutputStream out, String message, String testCase) {
        JUnitResultFormatter formatter = (JUnitResultFormatter) aFormatter;
        formatter.setOutput(out);
        formatter.startTestSuite(test);
        TestCase t = new VmExitErrorTest(message, test, testCase);
        formatter.startTest(t);
        formatter.addError(t, new AssertionFailedError(message));
        formatter.endTestSuite(test);
    }

    /** {@inheritDoc}. */
    public JUnitTaskMirror.JUnitTestRunnerMirror newJUnitTestRunner(JUnitTest test,
            boolean haltOnError, boolean filterTrace, boolean haltOnFailure,
            boolean showOutput, boolean logTestListenerEvents, AntClassLoader classLoader) {
        return new JUnitTestRunner(test, haltOnError, filterTrace, haltOnFailure,
                showOutput, logTestListenerEvents, classLoader);
    }

    /** {@inheritDoc}. */
    public JUnitTaskMirror.SummaryJUnitResultFormatterMirror newSummaryJUnitResultFormatter() {
        return new SummaryJUnitResultFormatter();
    }

    static class VmExitErrorTest extends TestCase {

        private String message;
        private JUnitTest test;
        private String testCase;

        VmExitErrorTest(String aMessage, JUnitTest anOriginalTest, String aTestCase) {
            message = aMessage;
            test = anOriginalTest;
            testCase = aTestCase;
        }

        public int countTestCases() {
            return 1;
        }

        public void run(TestResult r) {
            throw new AssertionFailedError(message);
        }

        public String getName() {
            return testCase;
        }

        String getClassName() {
            return test.getName();
        }

        public String toString() {
            return test.getName() + ":" + testCase;
        }
    }
}
