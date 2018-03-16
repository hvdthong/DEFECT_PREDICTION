package org.apache.ivy.ant;

import org.apache.ivy.core.cache.RepositoryCacheManager;
import org.apache.ivy.core.settings.IvySettings;
import org.apache.tools.ant.BuildException;

/**
 * Cleans the content of Ivy cache(s).
 */
public class IvyCleanCache extends IvyTask {
    public static final String ALL = "*";

    public static final String NONE = "NONE";

    private boolean resolution = true;
    
    private String cache = ALL;
    
    public String getCache() {
        return cache;
    }

    /**
     * Sets the name of the repository cache to clean, '*' for all caches, 'NONE' for no repository
     * cache cleaning at all.
     * 
     * @param cache
     *            the name of the cache to clean. Must not be <code>null</code>.
     */
    public void setCache(String cache) {
        this.cache = cache;
    }

    public boolean isResolution() {
        return resolution;
    }

    /**
     * Sets weither the resolution cache should be cleaned or not.
     * 
     * @param resolution
     *            <code>true</code> if the resolution cache should be cleaned, <code>false</code>
     *            otherwise.
     */
    public void setResolution(boolean resolution) {
        this.resolution = resolution;
    }


    public void doExecute() throws BuildException {
        IvySettings settings = getIvyInstance().getSettings();
        if (isResolution()) {
            settings.getResolutionCacheManager().clean();
        }
        if (ALL.equals(getCache())) {
            RepositoryCacheManager[] caches = settings.getRepositoryCacheManagers();
            for (int i = 0; i < caches.length; i++) {
                caches[i].clean();
            }
        } else if (!NONE.equals(getCache())) {
            RepositoryCacheManager cache = settings.getRepositoryCacheManager(getCache());
            if (cache == null) {
                throw new BuildException("unknown cache '" + getCache() + "'");
            } else {
                cache.clean();
            }
        }
    }
}
