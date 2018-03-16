package org.apache.ivy.plugins.conflict;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.ivy.core.module.descriptor.DependencyDescriptor;
import org.apache.ivy.core.resolve.IvyNode;
import org.apache.ivy.plugins.latest.ArtifactInfo;
import org.apache.ivy.plugins.latest.LatestStrategy;
import org.apache.ivy.util.Message;

public class LatestConflictManager extends AbstractConflictManager {
    public static class NoConflictResolvedYetException extends RuntimeException {
    }

    protected static final class IvyNodeArtifactInfo implements ArtifactInfo {
        private final IvyNode node;

        private IvyNodeArtifactInfo(IvyNode dep) {
            node = dep;
        }

        public long getLastModified() {
            long lastModified = node.getLastModified();
            if (lastModified == 0) {
                throw new NoConflictResolvedYetException();
            } else {
                return lastModified;
            }
        }

        public String getRevision() {
            return node.getResolvedId().getRevision();
        }

        public IvyNode getNode() {
            return node;
        }
    }

    private LatestStrategy strategy;

    private String strategyName;

    public LatestConflictManager() {
    }

    public LatestConflictManager(LatestStrategy strategy) {
        this.strategy = strategy;
    }

    public LatestConflictManager(String name, LatestStrategy strategy) {
        setName(name);
        this.strategy = strategy;
    }

    public Collection resolveConflicts(IvyNode parent, Collection conflicts) {
        if (conflicts.size() < 2) {
            return conflicts;
        }
        for (Iterator iter = conflicts.iterator(); iter.hasNext();) {
            IvyNode node = (IvyNode) iter.next();
            DependencyDescriptor dd = node.getDependencyDescriptor(parent);
            if (dd != null && dd.isForce()
                    && parent.getResolvedId().equals(dd.getParentRevisionId())) {
                return Collections.singleton(node);
            }
        }
        try {
            IvyNodeArtifactInfo latest = (IvyNodeArtifactInfo) 
                getStrategy().findLatest(toArtifactInfo(conflicts), null);
            if (latest != null) {
                return Collections.singleton(latest.getNode());
            } else {
                return conflicts;
            }
        } catch (NoConflictResolvedYetException ex) {
            return null;
        }
    }

    protected ArtifactInfo[] toArtifactInfo(Collection conflicts) {
        List artifacts = new ArrayList(conflicts.size());
        for (Iterator iter = conflicts.iterator(); iter.hasNext();) {
            IvyNode node = (IvyNode) iter.next();
            artifacts.add(new IvyNodeArtifactInfo(node));
        }
        return (ArtifactInfo[]) artifacts.toArray(new ArtifactInfo[artifacts.size()]);
    }

    public LatestStrategy getStrategy() {
        if (strategy == null) {
            if (strategyName != null) {
                strategy = getSettings().getLatestStrategy(strategyName);
                if (strategy == null) {
                    Message.error("unknown latest strategy: " + strategyName);
                    strategy = getSettings().getDefaultLatestStrategy();
                }
            } else {
                strategy = getSettings().getDefaultLatestStrategy();
            }
        }
        return strategy;
    }

    /**
     * To conform to configurator API
     * 
     * @param latestStrategy
     */
    public void setLatest(String strategyName) {
        this.strategyName = strategyName;
    }

    public void setStrategy(LatestStrategy strategy) {
        this.strategy = strategy;
    }

    public String toString() {
        return strategy != null ? String.valueOf(strategy) : strategyName;
    }
}
