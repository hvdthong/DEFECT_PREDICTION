package org.apache.ivy.ant;

import org.apache.ivy.Ivy;
import org.apache.ivy.core.IvyPatternHelper;
import org.apache.ivy.core.module.id.ModuleRevisionId;
import org.apache.ivy.core.settings.IvySettings;
import org.apache.ivy.plugins.matcher.PatternMatcher;
import org.apache.tools.ant.BuildException;

/**
 * Look for modules in the repository matching the given criteria, and sets a set of properties
 * according to what was found.
 */
public class IvyListModules extends IvyTask {
    private String organisation;

    private String module;

    private String branch = PatternMatcher.ANY_EXPRESSION;

    private String revision;

    private String matcher = PatternMatcher.EXACT_OR_REGEXP;

    private String property;

    private String value;

    public String getMatcher() {
        return matcher;
    }

    public void setMatcher(String matcher) {
        this.matcher = matcher;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String name) {
        this.property = name;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
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
        if (property == null) {
            throw new BuildException("no property provided for ivy findmodules");
        }
        if (value == null) {
            throw new BuildException("no value provided for ivy findmodules");
        }
        Ivy ivy = getIvyInstance();
        IvySettings settings = ivy.getSettings();
        ModuleRevisionId[] mrids = ivy.listModules(ModuleRevisionId.newInstance(organisation,
            module, branch, revision), settings.getMatcher(matcher));
        for (int i = 0; i < mrids.length; i++) {
            String name = IvyPatternHelper.substitute(settings.substitute(property), mrids[i]);
            String value = IvyPatternHelper.substitute(settings.substitute(this.value), mrids[i]);
            getProject().setProperty(name, value);
        }
    }
}
