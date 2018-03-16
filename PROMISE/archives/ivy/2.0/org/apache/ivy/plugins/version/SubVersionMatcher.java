package org.apache.ivy.plugins.version;

import java.util.Comparator;

import org.apache.ivy.core.module.id.ModuleRevisionId;

public class SubVersionMatcher extends AbstractVersionMatcher {
    public SubVersionMatcher() {
        super("sub-version");
    }

    public boolean isDynamic(ModuleRevisionId askedMrid) {
        return askedMrid.getRevision().endsWith("+");
    }

    public boolean accept(ModuleRevisionId askedMrid, ModuleRevisionId foundMrid) {
        String prefix = askedMrid.getRevision().substring(0, askedMrid.getRevision().length() - 1);
        return foundMrid.getRevision().startsWith(prefix);
    }

    public int compare(ModuleRevisionId askedMrid, ModuleRevisionId foundMrid,
            Comparator staticComparator) {
        if (foundMrid.getRevision().startsWith(
            askedMrid.getRevision().substring(0, askedMrid.getRevision().length() - 1))) {
            return 1;
        }
        return staticComparator.compare(askedMrid, foundMrid);
    }
}
