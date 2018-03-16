package org.apache.ivy.plugins.latest;

public interface ArtifactInfo {
    String getRevision();

    long getLastModified();
}
