package org.apache.ivy.ant;

import org.apache.ivy.core.module.id.ModuleRevisionId;

/**
 * Describes a mapping between a package name and an org name revision uple
 */
public class PackageMapping {
    private String pkg;

    private String organisation;

    private String module;

    private String revision;

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getPackage() {
        return pkg;
    }

    public void setPackage(String package1) {
        pkg = package1;
    }

    public ModuleRevisionId getModuleRevisionId() {
        return ModuleRevisionId.newInstance(organisation, module, revision);
    }
}
