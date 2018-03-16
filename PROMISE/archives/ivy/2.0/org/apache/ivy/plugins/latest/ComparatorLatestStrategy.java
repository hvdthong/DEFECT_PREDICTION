package org.apache.ivy.plugins.latest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ComparatorLatestStrategy extends AbstractLatestStrategy {

    private Comparator comparator;

    public ComparatorLatestStrategy() {
    }

    public ComparatorLatestStrategy(Comparator comparator) {
        this.comparator = comparator;
    }

    public List sort(ArtifactInfo[] infos) {
        List ret = new ArrayList(Arrays.asList(infos));
        Collections.sort(ret, comparator);
        return ret;
    }

    public Comparator getComparator() {
        return comparator;
    }

    public void setComparator(Comparator comparator) {
        this.comparator = comparator;
    }

}
