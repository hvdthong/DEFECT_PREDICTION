package org.apache.ivy.core.resolve;


/**
 * Information about a blacklisted module, providing context information in which it has been
 * blacklisted
 */
public class IvyNodeBlacklist {
    private IvyNode conflictParent;

    private IvyNode selectedNode;

    private IvyNode evictedNode;

    private IvyNode blacklistedNode;

    private String rootModuleConf;

    public IvyNodeBlacklist(IvyNode conflictParent, IvyNode selectedNode, IvyNode evictedNode,
            IvyNode blacklistedNode, String rootModuleConf) {
        this.conflictParent = conflictParent;
        this.selectedNode = selectedNode;
        this.evictedNode = evictedNode;
        this.blacklistedNode = blacklistedNode;
        this.rootModuleConf = rootModuleConf;
    }

    public IvyNode getConflictParent() {
        return conflictParent;
    }

    public IvyNode getSelectedNode() {
        return selectedNode;
    }

    public IvyNode getEvictedNode() {
        return evictedNode;
    }

    public IvyNode getBlacklistedNode() {
        return blacklistedNode;
    }

    public String getRootModuleConf() {
        return rootModuleConf;
    }

    public String toString() {
        return "[" + blacklistedNode + " blacklisted to evict " + evictedNode + " in favor of "
                + selectedNode + " in " + conflictParent + " for " + rootModuleConf + "]";
    }
}
