package org.apache.ivy.core.cache;

import java.io.File;

import org.apache.ivy.core.module.id.ModuleRevisionId;

public interface ResolutionCacheManager {
    public abstract File getResolutionCacheRoot();
    
    public abstract File getResolvedIvyFileInCache(ModuleRevisionId mrid);

    public abstract File getResolvedIvyPropertiesInCache(ModuleRevisionId mrid);

    public abstract File getConfigurationResolveReportInCache(String resolveId, String conf);

    public abstract File[] getConfigurationResolveReportsInCache(final String resolveId);
    
    /**
     * Cleans the whole cache.
     */
    public void clean();
}
