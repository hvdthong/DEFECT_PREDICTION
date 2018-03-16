package org.apache.ivy.core.install;

import java.util.Collection;

import org.apache.ivy.core.module.status.StatusManager;
import org.apache.ivy.plugins.matcher.PatternMatcher;
import org.apache.ivy.plugins.parser.ParserSettings;
import org.apache.ivy.plugins.report.ReportOutputter;
import org.apache.ivy.plugins.resolver.DependencyResolver;

public interface InstallEngineSettings extends ParserSettings {

    DependencyResolver getResolver(String from);

    Collection getResolverNames();

    ReportOutputter[] getReportOutputters();

    void setLogNotConvertedExclusionRule(boolean log);

    StatusManager getStatusManager();

    boolean logNotConvertedExclusionRule();

    PatternMatcher getMatcher(String matcherName);

    Collection getMatcherNames();

}
