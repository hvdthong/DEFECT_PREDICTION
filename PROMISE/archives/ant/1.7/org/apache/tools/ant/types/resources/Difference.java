package org.apache.tools.ant.types.resources;

import java.util.List;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.ResourceCollection;

/**
 * ResourceCollection representing the difference between
 * two or more nested ResourceCollections.
 * @since Ant 1.7
 */
public class Difference extends BaseResourceCollectionContainer {

    /**
     * Calculate the difference of the nested ResourceCollections.
     * @return a Collection of Resources.
     */
    protected Collection getCollection() {
        List rc = getResourceCollections();
        int size = rc.size();
        if (size < 2) {
            throw new BuildException("The difference of " + size
                + " resource collection" + ((size == 1) ? "" : "s")
                + " is undefined.");
        }
        HashSet hs = new HashSet();
        ArrayList al = new ArrayList();
        for (Iterator rcIter = rc.iterator(); rcIter.hasNext();) {
            for (Iterator r = nextRC(rcIter).iterator(); r.hasNext();) {
                Object next = r.next();
                if (hs.add(next)) {
                    al.add(next);
                } else {
                    al.remove(next);
                }
            }
        }
        return al;
    }

    private static ResourceCollection nextRC(Iterator i) {
        return (ResourceCollection) i.next();
    }

}
