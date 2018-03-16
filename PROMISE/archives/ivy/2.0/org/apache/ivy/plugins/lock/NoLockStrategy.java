package org.apache.ivy.plugins.lock;

import java.io.File;

import org.apache.ivy.core.module.descriptor.Artifact;

public class NoLockStrategy extends AbstractLockStrategy {
    public NoLockStrategy() {
        setName("no-lock");
    }

    public final boolean lockArtifact(Artifact artifact, File artifactFileToDownload) {
        return true;
    }

    public final void unlockArtifact(Artifact artifact, File artifactFileToDownload) {
    }
}
