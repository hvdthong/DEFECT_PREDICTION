package org.apache.tools.ant.types.resources;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.ResourceCollection;

/**
 * ResourceCollection representing the intersection
 * of multiple nested ResourceCollections.
 * @since Ant 1.7
 */
public class Intersect extends BaseResourceCollectionContainer {

    /**
     * Calculate the intersection of the nested ResourceCollections.
     * @return a Collection of Resources.
     */
    protected Collection getCollection() {
        List rcs = getResourceCollections();
        int size = rcs.size();
        if (size < 2) {
            throw new BuildException("The intersection of " + size
                + " resource collection" + ((size == 1) ? "" : "s")
                + " is undefined.");
        }
        ArrayList al = new ArrayList();
        Iterator rc = rcs.iterator();
        al.addAll(collect(rc.next()));
        while (rc.hasNext()) {
            al.retainAll(collect(rc.next()));
        }
        return al;
    }

    private ArrayList collect(Object o) {
        ArrayList result = new ArrayList();
        for (Iterator i = ((ResourceCollection) o).iterator(); i.hasNext();) {
            result.add(i.next());
        }
        return result;
    }
}
