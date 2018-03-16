package org.apache.ivy.plugins.latest;

import java.util.Date;
import java.util.List;
import java.util.ListIterator;

public abstract class AbstractLatestStrategy implements LatestStrategy {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }

    public ArtifactInfo findLatest(ArtifactInfo[] infos, Date date) {
        List l = sort(infos);

        for (ListIterator iter = l.listIterator(l.size()); iter.hasPrevious();) {
            ArtifactInfo info = (ArtifactInfo) iter.previous();
            if (date == null || info.getLastModified() < date.getTime()) {
                return info;
            }
        }
        return null;
    }
}
