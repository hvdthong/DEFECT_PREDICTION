package org.apache.ivy.plugins.resolver.util;

import org.apache.ivy.plugins.latest.LatestStrategy;

public interface HasLatestStrategy {
    public LatestStrategy getLatestStrategy();

    public void setLatestStrategy(LatestStrategy latestStrategy);

    public String getLatest();
}
