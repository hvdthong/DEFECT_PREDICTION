package org.apache.tools.ant.types.resources;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;

/**
 * ResourceCollection representing the union of multiple nested ResourceCollections.
 * @since Ant 1.7
 */
public class Union extends BaseResourceCollectionContainer {

    /**
     * Static convenience method to union an arbitrary set of Resources.
     * @param rc a ResourceCollection.
     * @return a Union.
     */
    public static Union getInstance(ResourceCollection rc) {
        return rc instanceof Union ? (Union) rc : new Union(rc);
    }

    /**
     * Default constructor.
     */
    public Union() {
    }

    /**
     * Convenience constructor.
     * @param rc the ResourceCollection to add.
     */
    public Union(ResourceCollection rc) {
        add(rc);
    }

    /**
     * Returns all Resources in String format. Provided for
     * convenience in implementing Path.
     * @return String array of Resources.
     */
    public String[] list() {
        if (isReference()) {
            return ((Union) getCheckedRef()).list();
        }
        Collection result = getCollection(true);
        return (String[]) (result.toArray(new String[result.size()]));
    }

    /**
     * Convenience method.
     * @return Resource[]
     */
    public Resource[] listResources() {
        if (isReference()) {
            return ((Union) getCheckedRef()).listResources();
        }
        Collection result = getCollection();
        return (Resource[]) (result.toArray(new Resource[result.size()]));
    }

    /**
     * Unify the contained Resources.
     * @return a Collection of Resources.
     */
    protected Collection getCollection() {
        return getCollection(false);
    }

    /**
     * Unify the contained Resources.
     * @param asString indicates whether the resulting Collection
     *        should contain Strings instead of Resources.
     * @return a Collection of Resources.
     */
    protected Collection getCollection(boolean asString) {
        List rc = getResourceCollections();
        if (rc.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        ArrayList union = new ArrayList(rc.size() * 2);
        Set set = new HashSet(rc.size() * 2);
        for (Iterator rcIter = rc.iterator(); rcIter.hasNext();) {
            for (Iterator r = nextRC(rcIter).iterator(); r.hasNext();) {
                Object o = r.next();
                if (asString) {
                    o = o.toString();
                }
                if (set.add(o)) {
                    union.add(o);
                }
            }
        }
        return union;
    }

    private static ResourceCollection nextRC(Iterator i) {
        return (ResourceCollection) i.next();
    }
}

