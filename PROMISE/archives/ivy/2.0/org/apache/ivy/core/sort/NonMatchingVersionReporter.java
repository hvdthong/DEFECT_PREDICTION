package org.apache.ivy.core.sort;

import org.apache.ivy.core.module.descriptor.DependencyDescriptor;
import org.apache.ivy.core.module.descriptor.ModuleDescriptor;

public interface NonMatchingVersionReporter {

    /**
     * Report to the user that ivy has detected that a module to sort has a dependency on an other
     * module to sort, but the revisions doesn't match.
     * 
     * @param descriptor
     *            The non matching dependency descriptor.
     * @param md
     *            The module to sort having the corect moduleID but a non matching revision
     */
    public void reportNonMatchingVersion(DependencyDescriptor descriptor, ModuleDescriptor md);

}
