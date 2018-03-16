package org.apache.ivy.plugins.resolver;

import java.util.Collection;

import org.apache.ivy.core.cache.RepositoryCacheManager;
import org.apache.ivy.core.module.id.ModuleId;
import org.apache.ivy.plugins.latest.LatestStrategy;
import org.apache.ivy.plugins.namespace.Namespace;
import org.apache.ivy.plugins.parser.ParserSettings;
import org.apache.ivy.plugins.version.VersionMatcher;

public interface ResolverSettings extends ParserSettings {

    LatestStrategy getLatestStrategy(String latestStrategyName);

    LatestStrategy getDefaultLatestStrategy();
    
    RepositoryCacheManager getRepositoryCacheManager(String name);
    
    RepositoryCacheManager getDefaultRepositoryCacheManager();
    
    RepositoryCacheManager[] getRepositoryCacheManagers();

    Namespace getNamespace(String namespaceName);

    Namespace getSystemNamespace();

    String getVariable(String string);

    void configureRepositories(boolean b);

    VersionMatcher getVersionMatcher();
    
    String getResolveMode(ModuleId moduleId);

    void filterIgnore(Collection names);

}
