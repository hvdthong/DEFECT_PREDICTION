package org.apache.ivy.core.sort;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.ivy.core.module.descriptor.DependencyDescriptor;
import org.apache.ivy.plugins.circular.CircularDependencyException;
import org.apache.ivy.plugins.circular.CircularDependencyStrategy;
import org.apache.ivy.plugins.version.VersionMatcher;
import org.apache.ivy.util.Message;

/**
 * Inner helper class for sorting ModuleDescriptors.<br>
 * ModuleDescriptorSorter use CollectionOfModulesToSort to find the dependencies of the modules, and
 * use ModuleInSort to store some temporary values attached to the modules to sort.
 * 
 * @see ModuleInSort
 * @see CollectionOfModulesToSort
 */
public class ModuleDescriptorSorter {

    private final CollectionOfModulesToSort moduleDescriptors;

    private final List sorted = new LinkedList();

    private final CircularDependencyStrategy circularDepStrategy;

    public ModuleDescriptorSorter(Collection modulesDescriptorsToSort, VersionMatcher matcher,
            NonMatchingVersionReporter nonMatchingVersionReporter,
            CircularDependencyStrategy circularDepStrategy) {
        this.circularDepStrategy = circularDepStrategy;
        moduleDescriptors = new CollectionOfModulesToSort(modulesDescriptorsToSort, matcher,
                nonMatchingVersionReporter);
    }

    /**
     * Iterates over all modules calling sortModuleDescriptorsHelp.
     * 
     * @return sorted module
     * @throws CircularDependencyException
     */
    public List sortModuleDescriptors() throws CircularDependencyException {
        Message.debug("Nbr of module to sort : " + moduleDescriptors.size());
        Iterator moduleDescriptorsIterator = moduleDescriptors.iterator();
        while (moduleDescriptorsIterator.hasNext()) {
            ModuleInSort next = (ModuleInSort) moduleDescriptorsIterator.next();
            sortModuleDescriptorsHelp(next, next);
        }
        return sorted;
    }

    /**
     * If current module has already been added to list, returns, Otherwise invokes
     * sortModuleDescriptorsHelp for all dependencies contained within set of moduleDescriptors.
     * Then finally adds self to list of sorted.<br/> When a loop is detected by a recursive call,
     * the moduleDescriptors are not added immediately added to the sorted list. They are added as
     * loop dependencies of the root, and will be added to the sorted list only when the root itself
     * will be added.
     * 
     * @param current
     *            Current module to add to sorted list.
     * @throws CircularDependencyException
     */
    private void sortModuleDescriptorsHelp(ModuleInSort current, ModuleInSort caller)
            throws CircularDependencyException {
        if (current.isProcessed()) {
            return;
        }
        if (current.checkLoop(caller, circularDepStrategy)) {
            return;
        }
        DependencyDescriptor[] descriptors = current.getDependencies();
        Message.debug("Sort dependencies of : " + current.toString()
                + " / Number of dependencies = " + descriptors.length);
        current.setCaller(caller);
        for (int i = 0; i < descriptors.length; i++) {
            ModuleInSort child = moduleDescriptors.getModuleDescriptorDependency(descriptors[i]);
            if (child != null) {
                sortModuleDescriptorsHelp(child, current);
            }
        }
        current.endOfCall();
        Message.debug("Sort done for : " + current.toString());
        current.addToSortedListIfRequired(sorted);
    }

}
