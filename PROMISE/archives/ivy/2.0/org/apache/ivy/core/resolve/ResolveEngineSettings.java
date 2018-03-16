package org.apache.ivy.core.resolve;

import org.apache.ivy.core.module.id.ModuleId;
import org.apache.ivy.core.module.id.ModuleRevisionId;
import org.apache.ivy.plugins.conflict.ConflictManager;
import org.apache.ivy.plugins.report.ReportOutputter;
import org.apache.ivy.plugins.resolver.DependencyResolver;
import org.apache.ivy.plugins.resolver.ResolverSettings;

public interface ResolveEngineSettings extends ResolverSettings {

    void setDictatorResolver(DependencyResolver dictatorResolver);

    boolean debugConflictResolution();

    ReportOutputter[] getReportOutputters();

    String getResolverName(ModuleRevisionId mid);

    boolean logNotConvertedExclusionRule();

    ConflictManager getConflictManager(ModuleId mid);

    boolean logModuleWhenFound();

    boolean logResolvedRevision();

}
