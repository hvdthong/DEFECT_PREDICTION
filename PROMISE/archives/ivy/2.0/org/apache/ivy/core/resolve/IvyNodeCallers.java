package org.apache.ivy.core.resolve;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.apache.ivy.core.module.descriptor.Artifact;
import org.apache.ivy.core.module.descriptor.DependencyDescriptor;
import org.apache.ivy.core.module.descriptor.ModuleDescriptor;
import org.apache.ivy.core.module.id.ModuleId;
import org.apache.ivy.core.module.id.ModuleRevisionId;

public class IvyNodeCallers {
    public static class Caller {
        private ModuleDescriptor md;

        private ModuleRevisionId mrid;


        private DependencyDescriptor dd;

        private boolean callerCanExclude;

        private boolean real = true;

        public Caller(ModuleDescriptor md, ModuleRevisionId mrid, DependencyDescriptor dd,
                boolean callerCanExclude) {
            this.md = md;
            this.mrid = mrid;
            this.dd = dd;
            this.callerCanExclude = callerCanExclude;
        }

        public void addConfiguration(String callerConf, String[] dependencyConfs) {
            String[] prevDepConfs = (String[]) confs.get(callerConf);
            if (prevDepConfs != null) {
                Set newDepConfs = new HashSet(Arrays.asList(prevDepConfs));
                newDepConfs.addAll(Arrays.asList(dependencyConfs));
                confs.put(callerConf, (String[]) newDepConfs
                        .toArray(new String[newDepConfs.size()]));
            } else {
                confs.put(callerConf, dependencyConfs);
            }
        }

        public String[] getCallerConfigurations() {
            return (String[]) confs.keySet().toArray(new String[confs.keySet().size()]);
        }

        public ModuleRevisionId getModuleRevisionId() {
            return mrid;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof Caller)) {
                return false;
            }
            Caller other = (Caller) obj;
            return other.confs.equals(confs) && mrid.equals(other.mrid);
        }

        public int hashCode() {
            int hash = 31;
            hash = hash * 13 + confs.hashCode();
            hash = hash * 13 + mrid.hashCode();
            return hash;
        }

        public String toString() {
            return mrid.toString();
        }

        public ModuleRevisionId getAskedDependencyId(ResolveData resolveData) {
            return dd.getDependencyRevisionId();
        }

        public ModuleDescriptor getModuleDescriptor() {
            return md;
        }

        public boolean canExclude() {
            return callerCanExclude || md.canExclude() || dd.canExclude();
        }

        public DependencyDescriptor getDependencyDescriptor() {
            return dd;
        }

        public void setRealCaller(boolean b) {
            this.real  = b;
        }
        
        public boolean isRealCaller() {
            return real;
        }
    }

    private Map callersByRootConf = new HashMap();



    private IvyNode node;

    public IvyNodeCallers(IvyNode node) {
        this.node = node;
    }

    /**
     * @param rootModuleConf
     * @param mrid
     * @param callerConf
     * @param dependencyConfs
     *            '*' must have been resolved
     * @param dd
     *            the dependency revision id asked by the caller
     */
    public void addCaller(String rootModuleConf, IvyNode callerNode, String callerConf,
            String[] dependencyConfs, DependencyDescriptor dd) {
        ModuleDescriptor md = callerNode.getDescriptor();
        ModuleRevisionId mrid = callerNode.getResolvedId();
        if (mrid.getModuleId().equals(node.getId().getModuleId())) {
            throw new IllegalArgumentException("a module is not authorized to depend on itself: "
                    + node.getId());
        }
        Map callers = (Map) callersByRootConf.get(rootModuleConf);
        if (callers == null) {
            callers = new HashMap();
            callersByRootConf.put(rootModuleConf, callers);
        }
        Caller caller = (Caller) callers.get(mrid);
        if (caller == null) {
            caller = new Caller(md, mrid, dd, callerNode.canExclude(rootModuleConf));
            callers.put(mrid, caller);
        }
        caller.addConfiguration(callerConf, dependencyConfs);

        IvyNode parent = callerNode.getRealNode();
        for (Iterator iter = parent.getAllCallersModuleIds().iterator(); iter.hasNext();) {
            ModuleId mid = (ModuleId) iter.next();
            allCallers.put(mid, parent);
        }
        allCallers.put(mrid.getModuleId(), callerNode);
    }
    
    void removeCaller(String rootModuleConf, ModuleRevisionId callerMrid) {
        allCallers.remove(callerMrid.getModuleId());
        Map callers = (Map) callersByRootConf.get(rootModuleConf);
        if (callers != null) {
            callers.remove(callerMrid);
        }
    }

    public Caller[] getCallers(String rootModuleConf) {
        Map callers = (Map) callersByRootConf.get(rootModuleConf);
        if (callers == null) {
            return new Caller[0];
        }
        return (Caller[]) callers.values().toArray(new Caller[callers.values().size()]);
    }

    public Caller[] getAllCallers() {
        Set all = new HashSet();
        for (Iterator iter = callersByRootConf.values().iterator(); iter.hasNext();) {
            Map callers = (Map) iter.next();
            all.addAll(callers.values());
        }
        return (Caller[]) all.toArray(new Caller[all.size()]);
    }

    public Caller[] getAllRealCallers() {
        Set all = new HashSet();
        for (Iterator iter = callersByRootConf.values().iterator(); iter.hasNext();) {
            Map callers = (Map) iter.next();
            for (Iterator iterator = callers.values().iterator(); iterator.hasNext();) {
                Caller c = (Caller) iterator.next();
                if (c.isRealCaller()) {
                    all.add(c);
                }
            }
        }
        return (Caller[]) all.toArray(new Caller[all.size()]);
    }

    public Collection getAllCallersModuleIds() {
        return allCallers.keySet();
    }

    void updateFrom(IvyNodeCallers callers, String rootModuleConf, boolean real) {
        Map nodecallers = (Map) callers.callersByRootConf.get(rootModuleConf);
        if (nodecallers != null) {
            Map thiscallers = (Map) callersByRootConf.get(rootModuleConf);
            if (thiscallers == null) {
                thiscallers = new HashMap();
                callersByRootConf.put(rootModuleConf, thiscallers);
            }
            for (Iterator iter = nodecallers.values().iterator(); iter.hasNext();) {
                Caller caller = (Caller) iter.next();
                if (!thiscallers.containsKey(caller.getModuleRevisionId())) {
                    if (!real) {
                        caller.setRealCaller(false);
                    }
                    thiscallers.put(caller.getModuleRevisionId(), caller);
                }
            }
        }
    }

    public IvyNode getDirectCallerFor(ModuleId from) {
        return (IvyNode) allCallers.get(from);
    }

    /**
     * Returns true if ALL callers exclude the given artifact in the given root module conf
     * 
     * @param rootModuleConf
     * @param artifact
     * @return
     */
    boolean doesCallersExclude(String rootModuleConf, Artifact artifact) {
        return doesCallersExclude(rootModuleConf, artifact, new Stack());
    }

    boolean doesCallersExclude(String rootModuleConf, Artifact artifact, Stack callersStack) {
        if (callersStack.contains(node.getId())) {
            return false;
        }
        callersStack.push(node.getId());
        try {
            Caller[] callers = getCallers(rootModuleConf);
            if (callers.length == 0) {
                return false;
            }
            for (int i = 0; i < callers.length; i++) {
                if (!callers[i].canExclude()) {
                    return false;
                }
                ModuleDescriptor md = callers[i].getModuleDescriptor();
                if (!doesExclude(md, rootModuleConf, callers[i].getCallerConfigurations(),
                    callers[i].getDependencyDescriptor(), artifact, callersStack)) {
                    return false;
                }
            }
            return true;
        } finally {
            callersStack.pop();
        }
    }

    private boolean doesExclude(ModuleDescriptor md, String rootModuleConf, String[] moduleConfs,
            DependencyDescriptor dd, Artifact artifact, Stack callersStack) {
        if (dd != null) {
            if (dd.doesExclude(moduleConfs, artifact.getId().getArtifactId())) {
                return true;
            }
        }
        if (md.doesExclude(moduleConfs, artifact.getId().getArtifactId())) {
            return true;
        }
        IvyNode c = node.getData().getNode(md.getModuleRevisionId());
        if (c != null) {
            return c.doesCallersExclude(rootModuleConf, artifact, callersStack);
        } else {
            return false;
        }
    }

}
