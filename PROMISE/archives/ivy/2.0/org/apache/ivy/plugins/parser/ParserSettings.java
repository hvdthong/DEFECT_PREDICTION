package org.apache.ivy.plugins.parser;

import java.io.File;
import java.util.Map;

import org.apache.ivy.core.RelativeUrlResolver;
import org.apache.ivy.core.cache.ResolutionCacheManager;
import org.apache.ivy.core.module.id.ModuleRevisionId;
import org.apache.ivy.core.module.status.StatusManager;
import org.apache.ivy.plugins.conflict.ConflictManager;
import org.apache.ivy.plugins.matcher.PatternMatcher;
import org.apache.ivy.plugins.namespace.Namespace;
import org.apache.ivy.plugins.resolver.DependencyResolver;

public interface ParserSettings {

    String substitute(String value);
    
    Map/*<String, String>*/ substitute(Map/*<String, String>*/ strings);
    
    ResolutionCacheManager getResolutionCacheManager();

    ConflictManager getConflictManager(String name);

    PatternMatcher getMatcher(String matcherName);

    Namespace getNamespace(String namespace);

    StatusManager getStatusManager();

    RelativeUrlResolver getRelativeUrlResolver();
    
    DependencyResolver getResolver(ModuleRevisionId mRevId);
    
    File resolveFile(String filename);
    
    File getBaseDir();

}
