package org.apache.ivy.core.sort;

import org.apache.ivy.plugins.circular.CircularDependencyStrategy;
import org.apache.ivy.plugins.version.VersionMatcher;

/**
 * The settings/collaborators used by the SortEngine.
 */
public interface SortEngineSettings {

    public CircularDependencyStrategy getCircularDependencyStrategy();
    
    public VersionMatcher getVersionMatcher();
}
