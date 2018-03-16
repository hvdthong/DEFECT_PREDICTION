package org.apache.ivy.plugins.report;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.ivy.core.cache.ResolutionCacheManager;
import org.apache.ivy.core.report.ConfigurationResolveReport;
import org.apache.ivy.core.report.ResolveReport;
import org.apache.ivy.core.resolve.ResolveOptions;
import org.apache.ivy.util.FileUtil;
import org.apache.ivy.util.Message;

/**
 * A Report outputter implementation using {@link XmlReportWriter} to write xml reports to the
 * resolution cache.
 */
public class XmlReportOutputter implements ReportOutputter {
    private XmlReportWriter writer = new XmlReportWriter();
    
    public String getName() {
        return XML;
    }

    public void output(
            ResolveReport report, ResolutionCacheManager cacheMgr, ResolveOptions options) 
            throws IOException {
        String[] confs = report.getConfigurations();
        for (int i = 0; i < confs.length; i++) {
            output(report.getConfigurationReport(confs[i]), report.getResolveId(), confs, cacheMgr);
        }
    }

    public void output(ConfigurationResolveReport report, String resolveId, 
            String[] confs, ResolutionCacheManager cacheMgr) 
            throws IOException {
        File reportFile = cacheMgr.getConfigurationResolveReportInCache(
            resolveId, report.getConfiguration());
        File reportParentDir = reportFile.getParentFile();
        reportParentDir.mkdirs();
        OutputStream stream = new FileOutputStream(reportFile);
        writer.output(report, confs, stream);
        stream.close();

        Message.verbose("\treport for " + report.getModuleDescriptor().getModuleRevisionId()
            + " " + report.getConfiguration() + " produced in " + reportFile);

        File reportXsl = new File(reportParentDir, "ivy-report.xsl");
        File reportCss = new File(reportParentDir, "ivy-report.css");
        if (!reportXsl.exists()) {
            FileUtil.copy(XmlReportOutputter.class.getResource("ivy-report.xsl"), reportXsl,
                null);
        }
        if (!reportCss.exists()) {
            FileUtil.copy(XmlReportOutputter.class.getResource("ivy-report.css"), reportCss,
                null);
        }
    }
}
