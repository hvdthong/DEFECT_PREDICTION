package org.apache.ivy.core.event.download;

import java.io.File;

import org.apache.ivy.core.cache.ArtifactOrigin;
import org.apache.ivy.core.module.descriptor.Artifact;
import org.apache.ivy.core.report.ArtifactDownloadReport;
import org.apache.ivy.plugins.resolver.DependencyResolver;

public class EndArtifactDownloadEvent extends DownloadEvent {
    public static final String NAME = "post-download-artifact";

    private DependencyResolver resolver;

    private ArtifactDownloadReport report;

    public EndArtifactDownloadEvent(DependencyResolver resolver, Artifact artifact,
            ArtifactDownloadReport report, File dest) {
        super(NAME, artifact);
        this.resolver = resolver;
        this.report = report;
        addAttribute("resolver", this.resolver.getName());
        addAttribute("status", this.report.getDownloadStatus().toString());
        addAttribute("details", this.report.getDownloadDetails());
        addAttribute("size", String.valueOf(this.report.getSize()));
        addAttribute("file", dest.getAbsolutePath());
        addAttribute("duration", String.valueOf(this.report.getDownloadTimeMillis()));
        ArtifactOrigin origin = report.getArtifactOrigin();
        if (!ArtifactOrigin.isUnknown(origin)) {
            addAttribute("origin", origin.getLocation());
            addAttribute("local", String.valueOf(origin.isLocal()));
        } else {
            addAttribute("origin", "");
            addAttribute("local", "");
        }
    }

    public ArtifactDownloadReport getReport() {
        return report;
    }

    public DependencyResolver getResolver() {
        return resolver;
    }

}
