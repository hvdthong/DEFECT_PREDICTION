package org.apache.ivy.core.cache;

import java.io.File;

import org.apache.ivy.core.module.descriptor.Artifact;
import org.apache.ivy.core.report.ArtifactDownloadReport;
import org.apache.ivy.plugins.resolver.util.ResolvedResource;

public interface DownloadListener {
    public void needArtifact(RepositoryCacheManager cache, Artifact artifact);
    public void startArtifactDownload(
            RepositoryCacheManager cache, ResolvedResource rres, 
            Artifact artifact, ArtifactOrigin origin);
    public void endArtifactDownload(
            RepositoryCacheManager cache, Artifact artifact, 
            ArtifactDownloadReport adr, File archiveFile);
}
