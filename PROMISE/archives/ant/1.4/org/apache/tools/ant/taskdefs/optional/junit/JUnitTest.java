package org.apache.tools.ant.taskdefs.optional.junit;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Commandline;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

/**
 * <p> Run a single JUnit test.
 *
 * <p> The JUnit test is actually run by {@link JUnitTestRunner}.
 * So read the doc comments for that class :)
 *
 * @author Thomas Haas
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>,
 * @author <a href="mailto:sbailliez@imediation.com">Stephane Bailliez</a>
 *
 * @see JUnitTask
 * @see JUnitTestRunner
 */
public class JUnitTest extends BaseTest {
    
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

    public JUnitTest(String name, boolean haltOnError, boolean haltOnFailure) {
        this.name  = name;
        this.haltOnError = haltOnError;
        this.haltOnFail = haltOnFail;
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

    public long runCount() {return runs;}
    public long failureCount() {return failures;}
    public long errorCount() {return errors;}
    public long getRunTime() {return runTime;}

    public Properties getProperties() { return props;}
    public void setProperties(Hashtable p) { 
        props = new Properties();  
        for (Enumeration enum = p.keys(); enum.hasMoreElements(); ) {
            Object key = enum.nextElement();
            props.put(key, p.get(key));
        }
    }

    public boolean shouldRun(Project p) {
        if (ifProperty != null && p.getProperty(ifProperty) == null) {
            return false;
        } else if (unlessProperty != null && 
                   p.getProperty(unlessProperty) != null) {
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
    void addFormattersTo(Vector v){
        final int count = formatters.size();
        for (int i = 0; i < count; i++){
            v.addElement( formatters.elementAt(i) );
        }
    }
}
