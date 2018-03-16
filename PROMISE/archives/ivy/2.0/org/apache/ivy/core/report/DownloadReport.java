package org.apache.ivy.core.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.ivy.core.module.descriptor.Artifact;

/**
 *
 */
public class DownloadReport {
    private Map artifacts = new HashMap();

    public void addArtifactReport(ArtifactDownloadReport adr) {
        artifacts.put(adr.getArtifact(), adr);
    }

    public ArtifactDownloadReport[] getArtifactsReports() {
        return (ArtifactDownloadReport[]) artifacts.values().toArray(
            new ArtifactDownloadReport[artifacts.size()]);
    }

    public ArtifactDownloadReport[] getArtifactsReports(DownloadStatus status) {
        List ret = new ArrayList(artifacts.size());
        for (Iterator iter = artifacts.values().iterator(); iter.hasNext();) {
            ArtifactDownloadReport adr = (ArtifactDownloadReport) iter.next();
            if (adr.getDownloadStatus() == status) {
                ret.add(adr);
            }
        }
        return (ArtifactDownloadReport[]) ret.toArray(new ArtifactDownloadReport[ret.size()]);
    }

    public ArtifactDownloadReport getArtifactReport(Artifact artifact) {
        return (ArtifactDownloadReport) artifacts.get(artifact);
    }
}
