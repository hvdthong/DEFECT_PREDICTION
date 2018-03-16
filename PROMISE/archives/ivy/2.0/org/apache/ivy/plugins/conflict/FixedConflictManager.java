package org.apache.ivy.plugins.conflict;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.apache.ivy.core.resolve.IvyNode;

public class FixedConflictManager extends AbstractConflictManager {
    private Collection revisions;

    public FixedConflictManager(String[] revs) {
        revisions = Arrays.asList(revs);
        setName("fixed" + revisions);
    }

    public Collection resolveConflicts(IvyNode parent, Collection conflicts) {
        Collection resolved = new ArrayList(conflicts.size());
        for (Iterator iter = conflicts.iterator(); iter.hasNext();) {
            IvyNode node = (IvyNode) iter.next();
            String revision = node.getResolvedId().getRevision();
            if (revisions.contains(revision)) {
                resolved.add(node);
            }
        }
        return resolved;
    }

    public Collection getRevs() {
        return revisions;
    }

}
