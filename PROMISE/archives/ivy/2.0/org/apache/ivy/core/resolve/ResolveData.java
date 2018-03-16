package org.apache.ivy.core.resolve;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.ivy.core.event.EventManager;
import org.apache.ivy.core.module.descriptor.DependencyDescriptor;
import org.apache.ivy.core.module.descriptor.ModuleDescriptor;
import org.apache.ivy.core.module.id.ModuleRevisionId;
import org.apache.ivy.core.report.ConfigurationResolveReport;
import org.apache.ivy.util.Message;

public class ResolveData {
    private ResolveEngine engine;

    
    private ConfigurationResolveReport report;

    private ResolveOptions options;

    private VisitNode currentVisitNode = null;

    private ResolvedModuleRevision currentResolvedModuleRevision;

    public ResolveData(ResolveData data, boolean validate) {
        this(data.engine, new ResolveOptions(data.options).setValidate(validate), 
            data.report, data.visitData);
        setCurrentVisitNode(data.currentVisitNode);
        setCurrentResolvedModuleRevision(data.currentResolvedModuleRevision);
    }

    public ResolveData(ResolveEngine engine, ResolveOptions options) {
        this(engine, options, null, new LinkedHashMap());
    }

    public ResolveData(ResolveEngine engine, ResolveOptions options,
            ConfigurationResolveReport report) {
        this(engine, options, report, new LinkedHashMap());
    }

    public ResolveData(ResolveEngine engine, ResolveOptions options,
            ConfigurationResolveReport report, Map visitData) {
        this.engine = engine;
        this.report = report;
        this.visitData = visitData;
        this.options = options;
    }

    public ConfigurationResolveReport getReport() {
        return report;
    }

    public IvyNode getNode(ModuleRevisionId mrid) {
        VisitData visitData = getVisitData(mrid);
        return visitData == null ? null : visitData.getNode();
    }

    public Collection getNodes() {
        Collection nodes = new ArrayList();
        for (Iterator iter = visitData.values().iterator(); iter.hasNext();) {
            VisitData vdata = (VisitData) iter.next();
            nodes.add(vdata.getNode());
        }
        return nodes;
    }

    public Collection getNodeIds() {
        return visitData.keySet();
    }

    public VisitData getVisitData(ModuleRevisionId mrid) {
        return (VisitData) visitData.get(mrid);
    }
    
    /**
     * Returns the VisitNode currently visited, or <code>null</code> if there is no node currently
     * visited in this context.
     * 
     * @return the VisitNode currently visited
     */
    public VisitNode getCurrentVisitNode() {
        return currentVisitNode;
    }
    
    /**
     * Sets the currently visited node. 
     * WARNING: This should only be called by Ivy core ResolveEngine!
     * 
     * @param currentVisitNode
     */
    void setCurrentVisitNode(VisitNode currentVisitNode) {
        this.currentVisitNode = currentVisitNode;
    }

    public void register(VisitNode node) {
        register(node.getId(), node);
    }

    public void register(ModuleRevisionId mrid, VisitNode node) {
        VisitData visitData = getVisitData(mrid);
        if (visitData == null) {
            visitData = new VisitData(node.getNode());
            visitData.addVisitNode(node);
            this.visitData.put(mrid, visitData);
        } else {
            visitData.setNode(node.getNode());
            visitData.addVisitNode(node);
        }
    }

    /**
     * Updates the visit data currently associated with the given mrid with the given node and the
     * visit nodes of the old visitData for the given rootModuleConf
     * 
     * @param mrid
     *            the module revision id for which the update should be done
     * @param node
     *            the IvyNode to associate with the visit data to update
     * @param rootModuleConf
     *            the root module configuration in which the update is made
     */
    void replaceNode(ModuleRevisionId mrid, IvyNode node, String rootModuleConf) {
        VisitData visitData = getVisitData(mrid);
        if (visitData == null) {
            throw new IllegalArgumentException("impossible to replace node for id " + mrid
                    + ". No registered node found.");
        }
        VisitData keptVisitData = getVisitData(node.getId());
        if (keptVisitData == null) {
            throw new IllegalArgumentException("impossible to replace node with " + node
                    + ". No registered node found for " + node.getId() + ".");
        }
        this.visitData.put(mrid, keptVisitData);
        keptVisitData.addVisitNodes(rootModuleConf, visitData.getVisitNodes(rootModuleConf));
        
        report.updateDependency(mrid, node);
    }

    public void setReport(ConfigurationResolveReport report) {
        this.report = report;
    }

    public Date getDate() {
        return options.getDate();
    }

    public boolean isValidate() {
        return options.isValidate();
    }

    public boolean isTransitive() {
        return options.isTransitive();
    }

    public ResolveOptions getOptions() {
        return options;
    }

    public ResolveEngineSettings getSettings() {
        return engine.getSettings();
    }

    public EventManager getEventManager() {
        return engine.getEventManager();
    }

    public ResolveEngine getEngine() {
        return engine;
    }

    void blacklist(IvyNode node) {
        for (Iterator iter = visitData.entrySet().iterator(); iter.hasNext();) {
            Entry entry = (Entry) iter.next();
            VisitData vdata = (VisitData) entry.getValue();
            if (vdata.getNode() == node && !node.getResolvedId().equals(entry.getKey())) {
                iter.remove();
            }
        }
    }

    public boolean isBlacklisted(String rootModuleConf, ModuleRevisionId mrid) {
        IvyNode node = getNode(mrid);
        return node != null && node.isBlacklisted(rootModuleConf);
    }


    public DependencyDescriptor mediate(DependencyDescriptor dd) {
        VisitNode current = getCurrentVisitNode();
        if (current != null) {
            DependencyDescriptor originalDD = dd;
            List dependers = new ArrayList(current.getPath());
            dependers.remove(dependers.size() - 1);
            Collections.reverse(dependers);
            for (Iterator iterator = dependers.iterator(); iterator.hasNext();) {
                VisitNode n = (VisitNode) iterator.next();
                ModuleDescriptor md = n.getDescriptor();
                if (md != null) {
                    dd = md.mediate(dd);
                }
            }
            if (originalDD != dd) {
                Message.verbose("dependency descriptor has been mediated: " 
                    + originalDD + " => " + dd);
            }
        }
        return getEngine().mediate(dd, getOptions());
    }

    /**
     * Sets the last {@link ResolvedModuleRevision} which has been currently resolved.
     * <p>
     * This can be used especially in dependency resolvers, to know if another dependency resolver
     * has already resolved the requested dependency, to take a decision if the resolver should try
     * to resolve it by itself or not. Indeed, the dependency resolver is responsible for taking
     * this decision, even when included in a chain. The chain responsibility is only to set this
     * current resolved module revision to enable the resolver to take the decision.
     * </p>
     * 
     * @param mr
     *            the last {@link ResolvedModuleRevision} which has been currently resolved.
     */
    public void setCurrentResolvedModuleRevision(ResolvedModuleRevision mr) {
        this.currentResolvedModuleRevision = mr;
    }
    
    /**
     * Returns the last {@link ResolvedModuleRevision} which has been currently resolved.
     * <p>
     * It can be <code>null</code>.
     * </p>
     * 
     * @return the last {@link ResolvedModuleRevision} which has been currently resolved.
     */
    public ResolvedModuleRevision getCurrentResolvedModuleRevision() {
        return currentResolvedModuleRevision;
    }
}
