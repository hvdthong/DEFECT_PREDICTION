package org.apache.ivy.core.report;

import java.io.File;

import org.apache.ivy.core.module.descriptor.Artifact;

public class MetadataArtifactDownloadReport extends ArtifactDownloadReport {
    private boolean isSearched;
    
    private File originalLocalFile;

    public MetadataArtifactDownloadReport(Artifact artifact) {
        super(artifact);
    }

    /**
     * Returns <code>true</code> if the resolution of this metadata artifact required at least one
     * access to the repository, or <code>false</code> if only provisioned data was used.
     * 
     * @return <code>true</code> if the resolution of this metadata artifact required at least one
     *         access to the repository
     */
    public boolean isSearched() {
        return isSearched;
    }

    public void setSearched(boolean isSearched) {
        this.isSearched = isSearched;
    }

    /**
     * Returns the location on the local filesystem where the original metadata artifact is
     * provisioned, or <code>null</code> if the provisioning failed.
     * 
     * @return the location on the local filesystem where the original metadata artifact is
     *         provisioned.
     */
    public File getOriginalLocalFile() {
        return originalLocalFile;
    }

    public void setOriginalLocalFile(File originalLocalFile) {
        this.originalLocalFile = originalLocalFile;
    }

}
