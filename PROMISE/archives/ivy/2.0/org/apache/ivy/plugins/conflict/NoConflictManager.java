package org.apache.ivy.plugins.conflict;

import java.util.Collection;

import org.apache.ivy.core.resolve.IvyNode;

public class NoConflictManager extends AbstractConflictManager {
    public NoConflictManager() {
        setName("all");
    }

    public Collection resolveConflicts(IvyNode parent, Collection conflicts) {
        return conflicts;
    }
}
