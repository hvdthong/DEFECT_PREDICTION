package org.apache.tools.ant.types.resources;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.ResourceCollection;

/**
 * ResourceCollection that contains the last <code>count</code> elements of
 * another ResourceCollection, a la the UNIX tail command.
 * @since Ant 1.7.1
 */
public class Last extends SizeLimitCollection {

    /**
     * Take the last <code>count</code> elements.
     * @return a Collection of Resources.
     */
    protected Collection getCollection() {
        int count = getValidCount();
        ResourceCollection rc = getResourceCollection();
        int i = count;
        Iterator iter = rc.iterator();
        int size = rc.size();
        for (; i < size; i++) {
            iter.next();
        }

        ArrayList al = new ArrayList(count);
        for (; iter.hasNext(); i++) {
            al.add(iter.next());
        }
        int found = al.size();
        if (found == count || (size < count && found == size)) {
            return al;
        }

        String msg = "Resource collection " + rc + " reports size " + size
            + " but returns " + i + " elements.";

        if (found > count) {
            log(msg, Project.MSG_WARN);
            return al.subList(found - count, found);
        }
        throw new BuildException(msg);
    }

}
