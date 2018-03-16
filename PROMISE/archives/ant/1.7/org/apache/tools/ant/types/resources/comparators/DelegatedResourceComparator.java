package org.apache.tools.ant.types.resources.comparators;

import java.util.Stack;
import java.util.Vector;
import java.util.Iterator;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Resource;

/**
 * Delegates to other ResourceComparators or, if none specified,
 * uses Resources' natural ordering.
 * @since Ant 1.7
 */
public class DelegatedResourceComparator extends ResourceComparator {

    private Vector v = null;

    /**
     * Add a delegate ResourceComparator.
     * @param c the next delegate ResourceComparator.
     */
    public synchronized void add(ResourceComparator c) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        if (c == null) {
            return;
        }
        v = (v == null) ? new Vector() : v;
        v.add(c);
    }

    /**
     * Equality method based on the vector of resources,
     * or if a reference, the referredto object.
     * @param o the object to check against.
     * @return true if there is equality.
     */
    public synchronized boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (isReference()) {
            return getCheckedRef().equals(o);
        }
        if (!(o instanceof DelegatedResourceComparator)) {
            return false;
        }
        Vector ov = ((DelegatedResourceComparator) o).v;
        return v == null ? ov == null : v.equals(ov);
    }

    /**
     * Hashcode based on the rules for equality.
     * @return a hashcode.
     */
    public synchronized int hashCode() {
        if (isReference()) {
            return getCheckedRef().hashCode();
        }
        return v == null ? 0 : v.hashCode();
    }

    /** {@inheritDoc} */
    protected synchronized int resourceCompare(Resource foo, Resource bar) {
        if (v == null || v.isEmpty()) {
            return foo.compareTo(bar);
        }
        int result = 0;
        for (Iterator i = v.iterator(); result == 0 && i.hasNext();) {
            result = ((ResourceComparator) i.next()).resourceCompare(foo, bar);
        }
        return result;
    }

    /**
     * Overrides the version from DataType to recurse on nested ResourceSelector
s.
     * @param stk the Stack of references.
     * @param p   the Project to resolve against.
     * @throws BuildException on error.
     */
    protected void dieOnCircularReference(Stack stk, Project p)
        throws BuildException {
        if (isChecked()) {
            return;
        }
        if (isReference()) {
            super.dieOnCircularReference(stk, p);
        } else {
            if (!(v == null || v.isEmpty())) {
                for (Iterator i = v.iterator(); i.hasNext();) {
                    Object o = i.next();
                    if (o instanceof DataType) {
                        stk.push(o);
                        invokeCircularReferenceCheck((DataType) o, stk, p);
                    }
                }
            }
            setChecked(true);
        }
    }
}
