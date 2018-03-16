package org.apache.ivy.core.event.download;

import org.apache.ivy.core.event.IvyEvent;
import org.apache.ivy.core.module.descriptor.Artifact;

public abstract class DownloadEvent extends IvyEvent {
    private Artifact artifact;

    public DownloadEvent(String name, Artifact artifact) {
        super(name);
        this.artifact = artifact;
        addArtifactAttributes(this.artifact);
    }

    protected void addArtifactAttributes(Artifact artifact) {
        addMridAttributes(artifact.getModuleRevisionId());
        addAttributes(artifact.getAttributes());
        addAttribute("metadata", String.valueOf(artifact.isMetadata()));
    }

    public Artifact getArtifact() {
        return artifact;
    }

}
