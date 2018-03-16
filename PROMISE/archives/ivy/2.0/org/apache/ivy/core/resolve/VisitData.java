package org.apache.ivy.core.resolve;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is used to store data related to one node of the dependency graph visit. It stores
 * both an {@link IvyNode} and related {@link VisitNode} objects. Indeed, during the visit of the
 * graph, the algorithm can visit the same node from several parents, thus requiring several
 * VisitNode.
 */
public class VisitData {
    /**
     * A node in the graph of module dependencies resolution
     */
    private IvyNode node;

    /**
     * The associated visit nodes, per rootModuleConf Note that the value is a List, because a node
     * can be visited from several parents during the resolution process
     */

    public VisitData(IvyNode node) {
        this.node = node;
    }

    public void addVisitNode(VisitNode node) {
        String rootModuleConf = node.getRootModuleConf();
        getVisitNodes(rootModuleConf).add(node);
    }

    public List getVisitNodes(String rootModuleConf) {
        List visits = (List) visitNodes.get(rootModuleConf);
        if (visits == null) {
            visits = new ArrayList();
            visitNodes.put(rootModuleConf, visits);
        }
        return visits;
    }

    public IvyNode getNode() {
        return node;
    }

    public void setNode(IvyNode node) {
        this.node = node;
    }

    public void addVisitNodes(String rootModuleConf, List visitNodes) {
        getVisitNodes(rootModuleConf).addAll(visitNodes);
    }
}
