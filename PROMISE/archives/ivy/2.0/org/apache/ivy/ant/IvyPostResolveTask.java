package org.apache.ivy.ant;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.ivy.Ivy;
import org.apache.ivy.core.cache.ResolutionCacheManager;
import org.apache.ivy.core.module.descriptor.ModuleDescriptor;
import org.apache.ivy.core.module.id.ModuleId;
import org.apache.ivy.core.module.id.ModuleRevisionId;
import org.apache.ivy.core.report.ResolveReport;
import org.apache.ivy.core.resolve.ResolveOptions;
import org.apache.ivy.core.settings.IvySettings;
import org.apache.ivy.util.Message;
import org.apache.ivy.util.StringUtils;
import org.apache.ivy.util.filter.Filter;
import org.apache.ivy.util.filter.FilterHelper;
import org.apache.tools.ant.BuildException;

/**
 * Base class for tasks needing to be performed after a resolve.
 */
public abstract class IvyPostResolveTask extends IvyTask {
    private String conf;

    private boolean haltOnFailure = true;

    private boolean transitive = true;

    private boolean inline = false;

    private String organisation;

    private String module;

    private String revision = "latest.integration";

    private String resolveId;

    private String type;

    private File file;

    private Filter artifactFilter = null;

    private boolean useOrigin = false;

    private Boolean keep = null;
    
    private String log = ResolveOptions.LOG_DEFAULT;

    public boolean isUseOrigin() {
        return useOrigin;
    }

    public void setUseOrigin(boolean useOrigin) {
        this.useOrigin = useOrigin;
    }
    
    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    protected void prepareAndCheck() {
        Ivy ivy = getIvyInstance();
        IvySettings settings = ivy.getSettings();

        boolean orgAndModSetManually = (organisation != null) && (module != null);

        organisation = getProperty(organisation, settings, "ivy.organisation");
        module = getProperty(module, settings, "ivy.module");

        if (file == null) {
            String fileName = getProperty(settings, "ivy.resolved.file", resolveId);
            if (fileName != null) {
                file = getProject().resolveFile(fileName);
            }
        }

        if (isInline()) {
            conf = conf == null ? "*" : conf;
            if (organisation == null) {
                throw new BuildException(
                        "no organisation provided for ivy cache task in inline mode: "
                        + "It can either be set explicitely via the attribute 'organisation' "
                        + "or via 'ivy.organisation' property");
            }
            if (module == null) {
                throw new BuildException(
                        "no module name provided for ivy cache task in inline mode: "
                        + "It can either be set explicitely via the attribute 'module' "
                        + "or via 'ivy.module' property");
            }
            String[] toResolve = getConfsToResolve(getOrganisation(), getModule() + "-caller",
                conf, true);
            for (int i = 0; i < toResolve.length; i++) {
                if ("*".equals(toResolve[i])) {
                    toResolve[i] = "*(public)";
                }
            }
            if (toResolve.length > 0) {
                Message.verbose("using inline mode to resolve " + getOrganisation() + " "
                        + getModule() + " " + getRevision() + " ("
                        + StringUtils.join(toResolve, ", ") + ")");
                IvyResolve resolve = createResolve(isHaltonfailure(), isUseOrigin());
                resolve.setOrganisation(getOrganisation());
                resolve.setModule(getModule());
                resolve.setRevision(getRevision());
                resolve.setInline(true);
                resolve.setConf(conf);
                resolve.setResolveId(resolveId);
                resolve.setTransitive(isTransitive());
                resolve.execute();
            } else {
                Message.verbose("inline resolve already done for " + getOrganisation() + " "
                        + getModule() + " " + getRevision() + " (" + conf + ")");
            }
            if ("*".equals(conf)) {
                conf = StringUtils.join(getResolvedConfigurations(getOrganisation(), getModule()
                        + "-caller", true), ", ");
            }
        } else {
            Message.debug("using standard ensure resolved");

            if (!orgAndModSetManually) {
                ensureResolved(settings);
            }

            conf = getProperty(conf, settings, "ivy.resolved.configurations");
            if ("*".equals(conf)) {
                conf = getProperty(settings, "ivy.resolved.configurations");
                if (conf == null) {
                    throw new BuildException(
                            "bad conf provided for ivy cache task: "
                            + "'*' can only be used with a prior call to <resolve/>");
                }
            }
        }
        organisation = getProperty(organisation, settings, "ivy.organisation");
        module = getProperty(module, settings, "ivy.module");
        if (organisation == null) {
            throw new BuildException(
                    "no organisation provided for ivy cache task: "
                    + "It can either be set explicitely via the attribute 'organisation' "
                    + "or via 'ivy.organisation' property or a prior call to <resolve/>");
        }
        if (module == null) {
            throw new BuildException(
                    "no module name provided for ivy cache task: "
                    + "It can either be set explicitely via the attribute 'module' "
                    + "or via 'ivy.module' property or a prior call to <resolve/>");
        }
        if (conf == null) {
            throw new BuildException(
                    "no conf provided for ivy cache task: "
                    + "It can either be set explicitely via the attribute 'conf' or "
                    + "via 'ivy.resolved.configurations' property or a prior call to <resolve/>");
        }

        artifactFilter = FilterHelper.getArtifactTypeFilter(type);
    }

    protected void ensureResolved(IvySettings settings) {
        String requestedConfigs = getProperty(getConf(), settings, "ivy.resolved.configurations");

        String[] confs = null;
        if (getResolveId() != null) {
            confs = getConfsToResolve(getResolveId(), requestedConfigs);
        } else {
            confs = getConfsToResolve(getOrganisation(), getModule(), requestedConfigs, false);
        }

        if (confs.length > 0) {
            IvyResolve resolve = createResolve(isHaltonfailure(), isUseOrigin());
            resolve.setFile(getFile());
            resolve.setTransitive(isTransitive());
            resolve.setConf(StringUtils.join(confs, ", "));
            resolve.setResolveId(getResolveId());
            resolve.execute();
        }
    }

    protected String[] getConfsToResolve(String org, String module, String conf, boolean strict) {
        ModuleDescriptor reference = (ModuleDescriptor) getResolvedDescriptor(org, module, strict);
        String[] rconfs = getResolvedConfigurations(org, module, strict);
        return getConfsToResolve(reference, conf, rconfs);
    }

    protected String[] getConfsToResolve(String resolveId, String conf) {
        ModuleDescriptor reference = (ModuleDescriptor) getResolvedDescriptor(resolveId, false);
        if (reference == null) {
            if (conf == null) {
                return new String[] {"*"};
            } else {
                return splitConfs(conf);
            }
        }
        String[] rconfs = (String[]) getProject().getReference(
            "ivy.resolved.configurations.ref." + resolveId);
        return getConfsToResolve(reference, conf, rconfs);
    }

    private String[] getConfsToResolve(ModuleDescriptor reference, String conf, String[] rconfs) {
        Message.debug("calculating configurations to resolve");

        if (reference == null) {
            Message.debug("module not yet resolved, all confs still need to be resolved");
            if (conf == null) {
                return new String[] {"*"};
            } else {
                return splitConfs(conf);
            }
        } else if (conf != null) {
            String[] confs;
            if ("*".equals(conf)) {
                confs = reference.getConfigurationsNames();
            } else {
                confs = splitConfs(conf);
            }
            
            HashSet rconfsSet = new HashSet(Arrays.asList(rconfs));
            
            ResolutionCacheManager cache = getSettings().getResolutionCacheManager();
            for (Iterator it = rconfsSet.iterator(); it.hasNext();) {
                String resolvedConf = (String) it.next();
                String resolveId = getResolveId();
                if (resolveId == null) {
                    resolveId = ResolveOptions.getDefaultResolveId(reference);
                }
                File report = cache.getConfigurationResolveReportInCache(resolveId, resolvedConf);
                if (!report.exists()) {
                    it.remove();
                }
            }
            
            HashSet confsSet = new HashSet(Arrays.asList(confs));
            Message.debug("resolved configurations:   " + rconfsSet);
            Message.debug("asked configurations:      " + confsSet);
            confsSet.removeAll(rconfsSet);
            Message.debug("to resolve configurations: " + confsSet);
            return (String[]) confsSet.toArray(new String[confsSet.size()]);
        } else {
            Message.debug("module already resolved, no configuration to resolve");
            return new String[0];
        }

    }

    protected IvyResolve createResolve(boolean haltOnFailure, boolean useOrigin) {
        Message.verbose("no resolved descriptor found: launching default resolve");
        IvyResolve resolve = new IvyResolve();
        resolve.setTaskName(getTaskName());
        resolve.setProject(getProject());
        resolve.setHaltonfailure(haltOnFailure);
        resolve.setUseOrigin(useOrigin);
        resolve.setValidate(isValidate());
        resolve.setKeep(isKeep());
        resolve.setLog(getLog());
        resolve.setSettingsRef(getSettingsRef());
        return resolve;
    }

    protected ModuleRevisionId getResolvedMrid() {
        return new ModuleRevisionId(getResolvedModuleId(), getRevision() == null ? Ivy
                .getWorkingRevision() : getRevision());
    }

    protected ModuleId getResolvedModuleId() {
        return isInline() ? new ModuleId(getOrganisation(), getModule() + "-caller")
                : new ModuleId(getOrganisation(), getModule());
    }

    protected ResolveReport getResolvedReport() {
        return getResolvedReport(getOrganisation(), isInline() ? getModule() + "-caller"
                : getModule(), resolveId);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getConf() {
        return conf;
    }

    public void setConf(String conf) {
        this.conf = conf;
    }

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

    public boolean isHaltonfailure() {
        return haltOnFailure;
    }

    public void setHaltonfailure(boolean haltOnFailure) {
        this.haltOnFailure = haltOnFailure;
    }

    public void setCache(File cache) {
        cacheAttributeNotSupported();
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String rev) {
        revision = rev;
    }

    public Filter getArtifactFilter() {
        return artifactFilter;
    }

    public boolean isTransitive() {
        return transitive;
    }

    public void setTransitive(boolean transitive) {
        this.transitive = transitive;
    }

    public boolean isInline() {
        return inline;
    }

    public void setInline(boolean inline) {
        this.inline = inline;
    }

    public void setResolveId(String resolveId) {
        this.resolveId = resolveId;
    }

    public String getResolveId() {
        return resolveId;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setKeep(boolean keep) {
        this.keep = Boolean.valueOf(keep);
    }

    public boolean isKeep() {
        return this.keep == null ? !isInline() : this.keep.booleanValue();
    }

}
