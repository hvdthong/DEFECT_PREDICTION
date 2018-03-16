package org.apache.tools.ant.taskdefs.optional.junit;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Commandline;

import java.io.File;
import java.util.Vector;

/**
 *
 * @author Thomas Haas
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>,
 * @author <a href="mailto:sbailliez@imediation.com">Stephane Bailliez</a>
 */
public class JUnitTest extends BaseTest {
    
    /** the name of the test case */
    private String name = null;
    
    /** the name of the result file */
    private String outfile = null;

    private long runs, failures, errors;
    private long runTime;

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

    public void setName(String value) {
        name = value;
    }

    public void setOutfile(String value) {
        outfile = value;
    }

    public String getName() {
        return name;
    }

    /**
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
