package org.apache.tools.ant.types.resources;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;

/**
 * ResourceCollection that contains the first <code>count</code> elements of
 * another ResourceCollection, a la the UNIX head command.
 * @since Ant 1.7
 */
public class First extends SizeLimitCollection {

    /**
     * Take the first <code>count</code> elements.
     * @return a Collection of Resources.
     */
    protected Collection getCollection() {
        int ct = getValidCount();
        Iterator iter = getResourceCollection().iterator();
        ArrayList al = new ArrayList(ct);
        for (int i = 0; i < ct && iter.hasNext(); i++) {
            al.add(iter.next());
        }
        return al;
    }

}
