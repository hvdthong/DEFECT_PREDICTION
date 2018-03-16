package org.apache.tools.ant.taskdefs.optional.junit;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import org.apache.tools.ant.Project;

/**
 * <p> Run a single JUnit test.
 *
 * <p> The JUnit test is actually run by {@link JUnitTestRunner}.
 * So read the doc comments for that class :)
 *
 * @since Ant 1.2
 *
 * @see JUnitTask
 * @see JUnitTestRunner
 */
public class JUnitTest extends BaseTest implements Cloneable {

    /** the name of the test case */
    private String name = null;

    /** the name of the result file */
    private String outfile = null;

    private long runs, failures, errors;
    private long runTime;

    private Properties props = null;

    public JUnitTest() {
    }

    public JUnitTest(String name) {
        this.name  = name;
    }

    public JUnitTest(String name, boolean haltOnError, boolean haltOnFailure,
                     boolean filtertrace) {
        this.name  = name;
        this.haltOnError = haltOnError;
        this.haltOnFail = haltOnFailure;
        this.filtertrace = filtertrace;
    }

    /**
     * Set the name of the test class.
     */
    public void setName(String value) {
        name = value;
    }

    /**
     * Set the name of the output file.
     */
    public void setOutfile(String value) {
        outfile = value;
    }

    /**
     * Get the name of the test class.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the name of the output file
     *
     * @return the name of the output file.
     */
    public String getOutfile() {
        return outfile;
    }

    public void setCounts(long runs, long failures, long errors) {
        this.runs = runs;
        this.failures = failures;
        this.errors = errors;
    }

    public void setRunTime(long runTime) {
        this.runTime = runTime;
    }

    public long runCount() {
        return runs;
    }

    public long failureCount() {
        return failures;
    }

    public long errorCount() {
        return errors;
    }

    public long getRunTime() {
        return runTime;
    }

    public Properties getProperties() {
        return props;
    }

    public void setProperties(Hashtable p) {
        props = new Properties();
        for (Enumeration e = p.keys(); e.hasMoreElements();) {
            Object key = e.nextElement();
            props.put(key, p.get(key));
        }
    }

    public boolean shouldRun(Project p) {
        if (ifProperty != null && p.getProperty(ifProperty) == null) {
            return false;
        } else if (unlessProperty != null
                    && p.getProperty(unlessProperty) != null) {
            return false;
        }

        return true;
    }

    public FormatterElement[] getFormatters() {
        FormatterElement[] fes = new FormatterElement[formatters.size()];
        formatters.copyInto(fes);
        return fes;
    }

    /**
     * Convenient method to add formatters to a vector
     */
    void addFormattersTo(Vector v) {
        final int count = formatters.size();
        for (int i = 0; i < count; i++) {
            v.addElement(formatters.elementAt(i));
        }
    }

    /**
     * @since Ant 1.5
     */
    public Object clone() {
        try {
            JUnitTest t = (JUnitTest) super.clone();
            t.props = props == null ? null : (Properties) props.clone();
            t.formatters = (Vector) formatters.clone();
            return t;
        } catch (CloneNotSupportedException e) {
            return this;
        }
    }
}
