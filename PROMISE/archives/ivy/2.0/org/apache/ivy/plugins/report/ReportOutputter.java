package org.apache.ivy.plugins.report;

import java.io.IOException;

import org.apache.ivy.core.cache.ResolutionCacheManager;
import org.apache.ivy.core.report.ResolveReport;
import org.apache.ivy.core.resolve.ResolveOptions;

/**
 *
 */
public interface ReportOutputter {
    public static final String CONSOLE = "console";

    public static final String XML = "xml";

    public abstract void output(
            ResolveReport report, ResolutionCacheManager cacheMgr, ResolveOptions options) 
            throws IOException;

    public abstract String getName();
}
