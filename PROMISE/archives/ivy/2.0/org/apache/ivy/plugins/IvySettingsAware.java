package org.apache.ivy.plugins;

import org.apache.ivy.core.settings.IvySettings;

public interface IvySettingsAware {
    void setSettings(IvySettings settings);
}
