package org.apache.ivy.plugins.latest;

import java.util.Comparator;

public class LatestTimeStrategy extends ComparatorLatestStrategy {
    private static final Comparator COMPARATOR = new Comparator() {
        public int compare(Object o1, Object o2) {
            long d1 = ((ArtifactInfo) o1).getLastModified();
            long d2 = ((ArtifactInfo) o2).getLastModified();
            return new Long(d1).compareTo(new Long(d2));
        }

    };

    public LatestTimeStrategy() {
        super(COMPARATOR);
        setName("latest-time");
    }
}
