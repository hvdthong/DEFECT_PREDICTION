package org.apache.ivy.core.repository;

import org.apache.ivy.core.resolve.ResolveEngineSettings;

public interface RepositoryManagementEngineSettings extends ResolveEngineSettings {
    public boolean dumpMemoryUsage();
}
