package org.apache.tools.ant.types.resources.comparators;

import java.util.Comparator;

import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Resource;

/**
 * Abstract Resource Comparator.
 * @since Ant 1.7
 */
public abstract class ResourceComparator extends DataType implements Comparator {

    /**
     * Compare two objects.
     * @param foo the first Object.
     * @param bar the second Object.
     * @return a negative integer, zero, or a positive integer as the first
     *         argument is less than, equal to, or greater than the second.
     * @throws ClassCastException if either argument is null.
     */
    public final int compare(Object foo, Object bar) {
        dieOnCircularReference();
        ResourceComparator c =
            isReference() ? (ResourceComparator) getCheckedRef() : this;
        return c.resourceCompare((Resource) foo, (Resource) bar);
    }

    /**
     * Test for equality with this ResourceComparator.
     * @param o the Object to compare against.
     * @return true if the specified Object equals this one.
     */
    public boolean equals(Object o) {
        if (isReference()) {
            return getCheckedRef().equals(o);
        }
        if (o == null) {
            return false;
        }
        return o == this || o.getClass().equals(getClass());
    }

    /**
     * Hashcode based on the rules for equality.
     * @return a hashcode.
     */
    public synchronized int hashCode() {
        if (isReference()) {
            return getCheckedRef().hashCode();
        }
        return getClass().hashCode();
    }

    /**
     * Compare two Resources.
     * @param foo the first Resource.
     * @param bar the second Resource.
     * @return a negative integer, zero, or a positive integer as the first
     *         argument is less than, equal to, or greater than the second.
     */
    protected abstract int resourceCompare(Resource foo, Resource bar);

}
