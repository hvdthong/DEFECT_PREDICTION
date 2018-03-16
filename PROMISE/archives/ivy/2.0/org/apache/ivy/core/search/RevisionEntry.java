package org.apache.ivy.core.search;

import org.apache.ivy.plugins.resolver.DependencyResolver;

public class RevisionEntry {
    private ModuleEntry moduleEntry;

    private String revision;

    public RevisionEntry(ModuleEntry mod, String name) {
        moduleEntry = mod;
        revision = name;
    }

    public ModuleEntry getModuleEntry() {
        return moduleEntry;
    }

    public String getRevision() {
        return revision;
    }

    public String getModule() {
        return moduleEntry.getModule();
    }

    public String getOrganisation() {
        return moduleEntry.getOrganisation();
    }

    public OrganisationEntry getOrganisationEntry() {
        return moduleEntry.getOrganisationEntry();
    }

    public DependencyResolver getResolver() {
        return moduleEntry.getResolver();
    }

    public String toString() {
        return moduleEntry + ";" + revision;
    }
}
