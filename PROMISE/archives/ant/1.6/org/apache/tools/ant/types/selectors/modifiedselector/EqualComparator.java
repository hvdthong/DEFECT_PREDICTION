package org.apache.tools.ant.types.selectors.modifiedselector;


import java.util.Comparator;


/**
 * Simple implementation of Comparator for use in CacheSelector.
 * compare() returns '0' (should not be selected) if both parameter
 * are equal otherwise '1' (should be selected).
 *
 * @version 2003-09-13
 * @since  Ant 1.6
 */
public class EqualComparator implements Comparator {

    /**
     * Implements Comparator.compare().
     * @param o1 the first object
     * @param o2 the second object
     * @return 0, if both are equal, otherwise 1
     */
    public int compare(Object o1, Object o2) {
        if (o1 == null) {
            if (o2 == null) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return (o1.equals(o2)) ? 0 : 1;
        }
    }

    /**
     * Override Object.toString().
     * @return information about this comparator
     */
    public String toString() {
        return "EqualComparator";
    }
}
