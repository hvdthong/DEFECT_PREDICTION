package org.apache.ivy.plugins.version;

import java.util.Comparator;

import org.apache.ivy.core.module.descriptor.ModuleDescriptor;
import org.apache.ivy.core.module.id.ModuleRevisionId;
import org.apache.ivy.core.module.status.StatusManager;

public class LatestVersionMatcher extends AbstractVersionMatcher {
    public LatestVersionMatcher() {
        super("latest");
    }

    public boolean isDynamic(ModuleRevisionId askedMrid) {
        return askedMrid.getRevision().startsWith("latest.");
    }

    public boolean accept(ModuleRevisionId askedMrid, ModuleRevisionId foundMrid) {
        return true;
    }

    public boolean needModuleDescriptor(ModuleRevisionId askedMrid, ModuleRevisionId foundMrid) {
        return !"latest.integration".equals(askedMrid.getRevision());
    }

    public boolean accept(ModuleRevisionId askedMrid, ModuleDescriptor foundMD) {
        String askedStatus = askedMrid.getRevision().substring("latest.".length());
        return StatusManager.getCurrent().getPriority(askedStatus) >= StatusManager.getCurrent()
                .getPriority(foundMD.getStatus());
    }

    /**
     * If we don't need a module descriptor we can consider the dynamic revision to be greater. If
     * we need a module descriptor then we can't know which one is greater and return 0.
     */
    public int compare(ModuleRevisionId askedMrid, ModuleRevisionId foundMrid,
            Comparator staticComparator) {
        return needModuleDescriptor(askedMrid, foundMrid) ? 0 : 1;
    }
}
