package org.apache.ivy.core.search;

import org.apache.ivy.plugins.resolver.DependencyResolver;

public class ModuleEntry {
    private OrganisationEntry organisationEntry;

    private String module;

    public ModuleEntry(OrganisationEntry org, String name) {
        organisationEntry = org;
        module = name;
    }

    public String getOrganisation() {
        return organisationEntry.getOrganisation();
    }

    public DependencyResolver getResolver() {
        return organisationEntry.getResolver();
    }

    public String getModule() {
        return module;
    }

    public OrganisationEntry getOrganisationEntry() {
        return organisationEntry;
    }

    public String toString() {
        return organisationEntry + "#" + module;
    }
}
