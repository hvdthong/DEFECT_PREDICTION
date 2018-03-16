package fr.jayasoft.ivy.resolver;

import fr.jayasoft.ivy.LatestStrategy;

public interface HasLatestStrategy {
    public LatestStrategy getLatestStrategy(); 
    public void setLatestStrategy(LatestStrategy latestStrategy);
    public String getLatest();
}
