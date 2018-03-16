package org.apache.ivy.core.event.download;

import org.apache.ivy.core.event.IvyEvent;
import org.apache.ivy.core.module.descriptor.Artifact;

public class PrepareDownloadEvent extends IvyEvent {
    public static final String NAME = "prepare-download";

    private Artifact[] artifacts;

    public PrepareDownloadEvent(Artifact[] artifacts) {
        super(NAME);
        this.artifacts = artifacts;
    }

    public Artifact[] getArtifacts() {
        return artifacts;
    }
}
