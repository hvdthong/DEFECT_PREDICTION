package org.apache.ivy.plugins.latest;

import java.util.Date;
import java.util.List;

public interface LatestStrategy {
    /**
     * Finds the latest artifact among the given artifacts info. The definition of 'latest' depends
     * on the strategy itself. Given artifacts info are all good candidate. If the given date is not
     * null, then found artifact should not be later than this date.
     * 
     * @param infos
     * @param date
     * @return the latest artifact among the given ones.
     */
    ArtifactInfo findLatest(ArtifactInfo[] infos, Date date);

    /**
     * Sorts the given artifacts info from the oldest one to the latest one. The definition of
     * 'latest' depends on the strategy itself. Given artifacts info are all good candidate.
     * 
     * @param infos
     * @return
     */
    List sort(ArtifactInfo[] infos);

    String getName();
}
