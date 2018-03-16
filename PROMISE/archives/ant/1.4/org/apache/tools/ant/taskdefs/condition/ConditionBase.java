package org.apache.tools.ant.taskdefs.condition;

import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Vector;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Available;
import org.apache.tools.ant.taskdefs.UpToDate;

/**
 * Baseclass for the &lt;condition&gt; task as well as several
 * conditions - ensures that the types of conditions inside the task
 * and the "container" conditions are in sync.
 *
 * @author <a href="mailto:stefan.bodewig@epost.de>Stefan Bodewig</a>
 * @version $Revision: 269456 $
 */
public abstract class ConditionBase {
    private Vector conditions = new Vector();
    private Project project;

    public void setProject(Project p) {
        this.project = p;
    }
    protected Project getProject() {return project;}

    /**
     * Count the conditions.
     *
     * @since 1.1
     */
    protected int countConditions() {return conditions.size();}

    /**
     * Iterate through all conditions.
     *
     * @since 1.1
     */
    protected final Enumeration getConditions() {
        return new ConditionEnumeration();
    }

    /**
     * Add an &lt;available&gt; condition.
     *
     * @since 1.1
     */
    public void addAvailable(Available a) {conditions.addElement(a);}

    /**
     * Add an &lt;uptodate&gt; condition.
     *
     * @since 1.1
     */
    public void addUptodate(UpToDate u) {conditions.addElement(u);}

    /**
     * Add an &lt;not&gt; condition "container".
     *
     * @since 1.1
     */
    public void addNot(Not n) {conditions.addElement(n);}

    /**
     * Add an &lt;and&gt; condition "container".
     *
     * @since 1.1
     */
    public void addAnd(And a) {conditions.addElement(a);}

    /**
     * Add an &lt;or&gt; condition "container".
     *
     * @since 1.1
     */
    public void addOr(Or o) {conditions.addElement(o);}

    /**
     * Add an &lt;equals&gt; condition.
     *
     * @since 1.1
     */
    public void addEquals(Equals e) {conditions.addElement(e);}

    /**
     * Add an &lt;os&gt; condition.
     *
     * @since 1.1
     */
    public void addOs(Os o) {conditions.addElement(o);}

    /**
     * Inner class that configures those conditions with a project
     * instance that need it.
     *
     * @since 1.1
     */
    private class ConditionEnumeration implements Enumeration {
        private int currentElement = 0;

        public boolean hasMoreElements() {
            return countConditions() > currentElement;
        }

        public Object nextElement() throws NoSuchElementException {
            Object o = null;
            try {
                o = conditions.elementAt(currentElement++);
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new NoSuchElementException();
            }
            
            if (o instanceof Task) {
                ((Task) o).setProject(getProject());
            } else if (o instanceof ConditionBase) {
                ((ConditionBase) o).setProject(getProject());
            }
            return o;
        }
    }
}
