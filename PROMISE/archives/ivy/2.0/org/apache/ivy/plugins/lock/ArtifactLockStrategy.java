package org.apache.ivy.plugins.lock;

import java.io.File;

import org.apache.ivy.core.module.descriptor.Artifact;

public class ArtifactLockStrategy extends FileBasedLockStrategy {
    public ArtifactLockStrategy() {
        init();
    }

    public ArtifactLockStrategy(boolean debugLocking) {
        super(debugLocking);
        init();
    }

    private void init() {
        setName("artifact-lock");
    }

    public boolean lockArtifact(Artifact artifact, File artifactFileToDownload) 
            throws InterruptedException {
        return acquireLock(new File(artifactFileToDownload.getAbsolutePath() + ".lck"));
    }

    public void unlockArtifact(Artifact artifact, File artifactFileToDownload) {
        releaseLock(new File(artifactFileToDownload.getAbsolutePath() + ".lck"));
    }
}
