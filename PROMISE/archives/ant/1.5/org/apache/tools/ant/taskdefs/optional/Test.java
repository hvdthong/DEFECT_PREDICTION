package org.apache.tools.ant.taskdefs.optional;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Java;
import java.util.Vector;

/**
 * This is a primitive task to execute a unit test in the
 * org.apache.testlet framework.
 * 
 * @deprecated testlet has been abandoned in favor of JUnit by the
 * Avalon community
 *
 * @author <a href="mailto:donaldp@apache.org">Peter Donald</a>
 * @ant.task ignore="true"
 */
public class Test extends Java {

    protected Vector m_tests = new Vector();


    /**
     * testlet to run
     */
    protected static final class TestletEntry {

        protected String m_testname = "";


        /** name of test. No property expansion takes place here */
        public void addText(final String testname) {
            m_testname += testname;
        }


        public String toString() {
            return m_testname;
        }
    }


    public Test() {
        setClassname("org.apache.testlet.engine.TextTestEngine");
    }


    /**
     * add a declaration of a testlet to run
     */
    public TestletEntry createTestlet() {
        final TestletEntry entry = new TestletEntry();

        m_tests.addElement(entry);
        return entry;
    }


    /**
     * a boolean value indicating whether tests should display a 
     * message on success; optional
     */
    
    public void setShowSuccess(final boolean showSuccess) {
        createArg().setValue("-s=" + showSuccess);
    }


    /**
     * a boolean value indicating whether a banner should be displayed 
     * when starting testlet engine; optional.
     */
    public void setShowBanner(final String showBanner) {
        createArg().setValue("-b=" + showBanner);
    }


    /**
     * a boolean indicating that a stack trace is displayed on 
     * error (but not normal failure); optional.
     */
    public void setShowTrace(final boolean showTrace) {
        createArg().setValue("-t=" + showTrace);
    }


    public void setForceShowTrace(final boolean forceShowTrace) {
        createArg().setValue("-f=" + forceShowTrace);
    }


    public void execute()
         throws BuildException {

        final int size = m_tests.size();

        for (int i = 0; i < size; i++) {
            createArg().setValue(m_tests.elementAt(i).toString());
        }

        super.execute();
    }
}


