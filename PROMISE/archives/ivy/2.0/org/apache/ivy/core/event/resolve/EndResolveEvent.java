package org.apache.ivy.core.event.resolve;

import org.apache.ivy.core.module.descriptor.ModuleDescriptor;
import org.apache.ivy.core.report.ResolveReport;

public class EndResolveEvent extends ResolveEvent {
    public static final String NAME = "post-resolve";

    private ResolveReport report;

    public EndResolveEvent(ModuleDescriptor md, String[] confs, ResolveReport report) {
        super(NAME, md, confs);
        this.report = report;
        addAttribute("resolve-id", String.valueOf(report.getResolveId()));
        addAttribute("nb-dependencies", String.valueOf(report.getDependencies().size()));
        addAttribute("nb-artifacts", String.valueOf(report.getArtifacts().size()));
        addAttribute("resolve-duration", String.valueOf(report.getResolveTime()));
        addAttribute("download-duration", String.valueOf(report.getDownloadTime()));
        addAttribute("download-size", String.valueOf(report.getDownloadSize()));
    }

    public ResolveReport getReport() {
        return report;
    }

}
