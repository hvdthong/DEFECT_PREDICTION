package org.apache.ivy.plugins.conflict;

import java.util.Collection;

import org.apache.ivy.core.module.descriptor.DependencyDescriptor;
import org.apache.ivy.core.resolve.IvyNode;

public interface ConflictManager {
    /**
     * Resolves the eventual conflicts found in the given collection of IvyNode. This method return
     * a Collection of IvyNode which have not been evicted. The given conflicts Collection contains
     * at least one IvyNode. This method can be called with IvyNodes which are not yet loaded. If
     * this conflict manager is not able to resolve conflicts with the current data found in the
     * IvyNodes and need them to be fully loaded, it will return null to indicate that no conflict
     * resolution has been done.
     * 
     * @param parent
     *            the ivy node parent for which the conflict is to be resolved
     * @param conflicts
     *            the collection of IvyNode to check for conflicts
     * @return a Collection of IvyNode which have not been evicted, or null if conflict management
     *         resolution is not possible yet
     */
    Collection resolveConflicts(IvyNode parent, Collection conflicts);

    String getName();

    /**
     * Method called when all revisions available for a version constraint have been blacklisted,
     * and thus the dependency can't be resolved.
     * <p>
     * This will never happen if the conflict manager doesn't blacklist any module, so providing an
     * empty implementation in this case is fine.
     * </p>
     * 
     * @param dd
     *            the dependency descriptor for which all revisions are blacklisted.
     * @param foundBlacklisted
     *            the list of all ModuleRevisionId found which are blacklisted
     */
    void handleAllBlacklistedRevisions(
            DependencyDescriptor dd, Collection/*<ModuleRevisionId>*/ foundBlacklisted);
}
