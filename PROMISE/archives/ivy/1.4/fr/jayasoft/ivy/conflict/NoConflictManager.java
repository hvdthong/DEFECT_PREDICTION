package fr.jayasoft.ivy.conflict;

import java.util.Collection;

import fr.jayasoft.ivy.IvyNode;

public class NoConflictManager extends AbstractConflictManager {
    public NoConflictManager() {
        setName("all");
    }
    public Collection resolveConflicts(IvyNode parent, Collection conflicts) {
        return conflicts;
    }
}
