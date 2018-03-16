package org.apache.ivy.plugins.conflict;

import java.util.Collection;

import org.apache.ivy.core.module.descriptor.DependencyDescriptor;
import org.apache.ivy.core.settings.IvySettings;
import org.apache.ivy.plugins.IvySettingsAware;

public abstract class AbstractConflictManager implements ConflictManager, IvySettingsAware {
    private String name;

    private IvySettings settings;

    public IvySettings getSettings() {
        return settings;
    }

    public void setSettings(IvySettings settings) {
        this.settings = settings;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
    
    public void handleAllBlacklistedRevisions(
            DependencyDescriptor dd, Collection foundBlacklisted) {
    }
}
