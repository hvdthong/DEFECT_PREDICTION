package org.apache.ivy.plugins.version;

import java.util.Comparator;

import org.apache.ivy.core.module.descriptor.ModuleDescriptor;
import org.apache.ivy.core.module.id.ModuleRevisionId;
import org.apache.ivy.core.settings.IvySettings;
import org.apache.ivy.plugins.IvySettingsAware;
import org.apache.ivy.util.Checks;

public abstract class AbstractVersionMatcher implements VersionMatcher, IvySettingsAware {
    private String name;

    private IvySettings settings;

    public AbstractVersionMatcher() {
    }

    public AbstractVersionMatcher(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean needModuleDescriptor(ModuleRevisionId askedMrid, ModuleRevisionId foundMrid) {
        return false;
    }

    public boolean accept(ModuleRevisionId askedMrid, ModuleDescriptor foundMD) {
        return accept(askedMrid, foundMD.getResolvedModuleRevisionId());
    }

    /**
     * This method should be overriden in most cases, because it uses the default contract to return
     * 1 when it's not possible to know which revision is greater.
     */
    public int compare(ModuleRevisionId askedMrid, ModuleRevisionId foundMrid,
            Comparator staticComparator) {
        return 0;
    }

    public String toString() {
        return getName();
    }

    public IvySettings getSettings() {
        return settings;
    }

    public void setSettings(IvySettings settings) {
        Checks.checkNotNull(settings, "settings");
        this.settings = settings;
    }

}
