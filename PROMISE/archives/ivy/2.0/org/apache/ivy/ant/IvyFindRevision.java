package org.apache.ivy.ant;

import org.apache.ivy.Ivy;
import org.apache.ivy.core.module.id.ModuleId;
import org.apache.ivy.core.module.id.ModuleRevisionId;
import org.apache.ivy.core.resolve.ResolvedModuleRevision;
import org.apache.ivy.core.settings.IvySettings;
import org.apache.tools.ant.BuildException;

/**
 * Look for the latest module in the repository matching the given criteria, and sets a set of
 * properties according to what was found.
 */
public class IvyFindRevision extends IvyTask {
    private String organisation;

    private String module;

    private String branch;

    private String revision;

    private String property = "ivy.revision";

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

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String prefix) {
        this.property = prefix;
    }

    public void doExecute() throws BuildException {
        if (organisation == null) {
            throw new BuildException("no organisation provided for ivy findmodules");
        }
        if (module == null) {
            throw new BuildException("no module name provided for ivy findmodules");
        }
        if (revision == null) {
            throw new BuildException("no revision provided for ivy findmodules");
        }

        Ivy ivy = getIvyInstance();
        IvySettings settings = ivy.getSettings();
        if (branch == null) {
            settings.getDefaultBranch(new ModuleId(organisation, module));
        }
        ResolvedModuleRevision rmr = ivy.findModule(ModuleRevisionId.newInstance(organisation,
            module, branch, revision));
        if (rmr != null) {
            getProject().setProperty(property, rmr.getId().getRevision());
        }
    }
}
