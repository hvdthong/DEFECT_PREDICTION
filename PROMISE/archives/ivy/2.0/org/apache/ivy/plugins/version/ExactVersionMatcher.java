package org.apache.ivy.plugins.version;

import org.apache.ivy.core.module.id.ModuleRevisionId;

public class ExactVersionMatcher extends AbstractVersionMatcher {

    public ExactVersionMatcher() {
        super("exact");
    }

    public boolean isDynamic(ModuleRevisionId askedMrid) {
        return false;
    }

    public boolean accept(ModuleRevisionId askedMrid, ModuleRevisionId foundMrid) {
        return askedMrid.getRevision().equals(foundMrid.getRevision());
    }
}
