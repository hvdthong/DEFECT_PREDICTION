package org.apache.tools.ant.taskdefs.condition;

import java.util.Enumeration;
import java.util.Vector;


import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.taskdefs.Available;
import org.apache.tools.ant.taskdefs.Checksum;
import org.apache.tools.ant.taskdefs.UpToDate;

/**
 * Baseclass for the &lt;condition&gt; task as well as several
 * conditions - ensures that the types of conditions inside the task
 * and the "container" conditions are in sync.
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 * @since Ant 1.4
 * @version $Revision: 274041 $
 */
public abstract class ConditionBase extends ProjectComponent {
    private Vector conditions = new Vector();

    /**
     * Count the conditions.
     *
     * @since 1.1
     */
    protected int countConditions() {
        return conditions.size();
    }

    /**
     * Iterate through all conditions.
     *
     * @since 1.1
     */
    protected final Enumeration getConditions() {
        return conditions.elements();
    }

    /**
     * Add an &lt;available&gt; condition.
     *
     * @since 1.1
     */
    public void addAvailable(Available a) {conditions.addElement(a);}

    /**
     * Add an &lt;checksum&gt; condition.
     *
     * @since 1.4, Ant 1.5
     */
    public void addChecksum(Checksum c) {conditions.addElement(c);}

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
     * Add an &lt;isset&gt; condition.
     *
     * @since Ant 1.5
     */
    public void addIsSet(IsSet i) {conditions.addElement(i);}

    /**
     * Add an &lt;http&gt; condition.
     *
     * @since Ant 1.5
     */
    public void addHttp(Http h) {conditions.addElement(h);}

    /**
     * Add a &lt;socket&gt; condition.
     *
     * @since Ant 1.5
     */
    public void addSocket(Socket s) {conditions.addElement(s);}

    /**
     * Add a &lt;filesmatch&gt; condition.
     *
     * @since Ant 1.5
     */
    public void addFilesMatch(FilesMatch test) {conditions.addElement(test);}
    
    /**
     * Add a &lt;contains&gt; condition.
     *
     * @since Ant 1.5
     */
    public void addContains(Contains test) {conditions.addElement(test);}

    /**
     * Add a &lt;istrue&gt; condition.
     *
     * @since Ant 1.5
     */
    public void addIsTrue(IsTrue test) {conditions.addElement(test);}

    /**
     * Add a &lt;isfalse&gt; condition.
     *
     * @since Ant 1.5
     */
    public void addIsFalse(IsFalse test) {conditions.addElement(test);}
      
}
