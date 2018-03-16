package org.apache.ivy.core.sort;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.ivy.core.module.descriptor.ModuleDescriptor;
import org.apache.ivy.core.resolve.IvyNode;
import org.apache.ivy.plugins.circular.CircularDependencyStrategy;
import org.apache.ivy.plugins.circular.IgnoreCircularDependencyStrategy;
import org.apache.ivy.plugins.version.VersionMatcher;
import org.apache.ivy.util.Checks;

public class SortEngine {

    private SortEngineSettings settings;

    public SortEngine(SortEngineSettings settings) {
        if (settings == null) {
            throw new NullPointerException("SortEngine.settings can not be null");
        }
        this.settings = settings;
    }

    

    public List sortNodes(Collection nodes, SortOptions options) {
        /*
         * here we want to use the sort algorithm which work on module descriptors : so we first put
         * dependencies on a map from descriptors to dependency, then we sort the keySet (i.e. a
         * collection of descriptors), then we replace in the sorted list each descriptor by the
         * corresponding dependency
         */

        Map dependenciesMap = new LinkedHashMap();
        List nulls = new ArrayList();
        for (Iterator iter = nodes.iterator(); iter.hasNext();) {
            IvyNode node = (IvyNode) iter.next();
            if (node.getDescriptor() == null) {
                nulls.add(node);
            } else {
                List n = (List) dependenciesMap.get(node.getDescriptor());
                if (n == null) {
                    n = new ArrayList();
                    dependenciesMap.put(node.getDescriptor(), n);
                }
                n.add(node);
            }
        }
        List list = sortModuleDescriptors(dependenciesMap.keySet(), options);
        final double adjustFactor = 1.3;
        List ret = new ArrayList((int) (list.size() * adjustFactor + nulls.size()));
        for (int i = 0; i < list.size(); i++) {
            ModuleDescriptor md = (ModuleDescriptor) list.get(i);
            List n = (List) dependenciesMap.get(md);
            ret.addAll(n);
        }
        ret.addAll(0, nulls);
        return ret;
    }

    /**
     * Sorts the given ModuleDescriptors from the less dependent to the more dependent. This sort
     * ensures that a ModuleDescriptor is always found in the list before all ModuleDescriptors
     * depending directly on it.
     * 
     * @param moduleDescriptors
     *            a Collection of ModuleDescriptor to sort
     * @param options
     *            Options to use to sort the descriptors.
     * @return a List of sorted ModuleDescriptors
     * @throws CircularDependencyException
     *             if a circular dependency exists and circular dependency strategy decide to throw
     *             an exception
     */
    public List sortModuleDescriptors(Collection moduleDescriptors, SortOptions options) {
        Checks.checkNotNull(options, "options");
        ModuleDescriptorSorter sorter = new ModuleDescriptorSorter(moduleDescriptors,
                getVersionMatcher(), options.getNonMatchingVersionReporter(), 
                options.isUseCircularDependencyStrategy() 
                    ? getCircularStrategy() : IgnoreCircularDependencyStrategy.getInstance());
        return sorter.sortModuleDescriptors();
    }



    protected CircularDependencyStrategy getCircularStrategy() {
        return settings.getCircularDependencyStrategy();
    }

    protected VersionMatcher getVersionMatcher() {
        return settings.getVersionMatcher();
    }

}
